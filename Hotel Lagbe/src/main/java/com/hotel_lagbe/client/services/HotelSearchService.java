package com.hotel_lagbe.client.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hotel_lagbe.shared.models.HotelResult;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * HotelSearchService using Geoapify Places API (Free tier friendly)
 * Category: accommodation (hotels, guest houses, etc.)
 */
public class HotelSearchService {

    // ================== YOUR GEOAPIFY API KEY ==================
    private static final String API_KEY = "10488e7be3a541dab8d5f6f09559ded1";
    // ===========================================================

    private static final String PLACES_URL = "https://api.geoapify.com/v2/places";

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public HotelSearchService() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Search for hotels/accommodations near the given location
     */
//    public List<HotelResult> searchHotels(String location, int radiusMeters) {
//        List<HotelResult> results = new ArrayList<>();
//
//        try {
//            // Step 1: Geocode the location name to get lat/lng
//            double[] coords = geocodeLocation(location);
//            if (coords == null) {
//                System.err.println("Could not geocode location: " + location);
//                return results;
//            }
//
//            double lat = coords[0];
//            double lng = coords[1];
//            System.out.println("Geocoded '" + location + "' → lat=" + lat + ", lng=" + lng);
//
//            // Step 2: Search for accommodations using Geoapify Places API
//            results = fetchNearbyAccommodations(lat, lng, radiusMeters);
//            System.out.println("Found " + results.size() + " accommodations near " + location);
//
//        } catch (Exception e) {
//            System.err.println("HotelSearchService error: " + e.getMessage());
//            e.printStackTrace();
//        }
//
//        return results;
//    }
    public List<HotelResult> searchHotels(String location, int radiusMeters) {
        List<HotelResult> results = new ArrayList<>();

        try {
            double[] coords = geocodeLocation(location);
            if (coords == null) {
                System.err.println("Could not geocode location: " + location);
                return results;
            }

            double lat = coords[0];
            double lng = coords[1];
            System.out.println("Geocoded '" + location + "' → lat=" + lat + ", lng=" + lng);

            // Increased radius to 15 km for better coverage in Bangladesh
            results = fetchNearbyAccommodations(lat, lng, 15000);
            System.out.println("Found " + results.size() + " accommodations near " + location);

        } catch (Exception e) {
            System.err.println("HotelSearchService error: " + e.getMessage());
            e.printStackTrace();
        }

        return results;
    }

    private double[] geocodeLocation(String location) throws Exception {
        String encoded = URLEncoder.encode(location + ", Bangladesh", StandardCharsets.UTF_8);
        String url = "https://api.geoapify.com/v1/geocode/search?text=" + encoded + "&apiKey=" + API_KEY;

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            System.err.println("Geoapify Geocoding error: HTTP " + response.statusCode());
            return null;
        }

        JsonNode root = objectMapper.readTree(response.body());
        JsonNode features = root.get("features");

        if (features == null || features.isEmpty()) {
            return null;
        }

        JsonNode geometry = features.get(0).get("geometry");
        JsonNode coordinates = geometry.get("coordinates");

        double lng = coordinates.get(0).asDouble();
        double lat = coordinates.get(1).asDouble();

        return new double[]{lat, lng};
    }

    private List<HotelResult> fetchNearbyAccommodations(double lat, double lng, int radius) throws Exception {
        List<HotelResult> hotels = new ArrayList<>();

        String url = PLACES_URL
                + "?categories=accommodation"
                + "&filter=circle:" + lng + "," + lat + "," + radius
                + "&limit=20"
                + "&apiKey=" + API_KEY;

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            System.err.println("Geoapify Places error: HTTP " + response.statusCode());
            return hotels;
        }

        JsonNode root = objectMapper.readTree(response.body());
        JsonNode features = root.get("features");

        if (features == null) return hotels;

        for (JsonNode feature : features) {
            HotelResult hotel = parseHotelFromJson(feature);
            if (hotel != null) {
                hotels.add(hotel);
            }
        }

        return hotels;
    }

    private HotelResult parseHotelFromJson(JsonNode feature) {
        try {
            HotelResult hotel = new HotelResult();

            JsonNode properties = feature.get("properties");

            hotel.setName(getTextSafe(properties, "name"));
            hotel.setAddress(getTextSafe(properties, "formatted"));
            hotel.setPlaceId(getTextSafe(properties, "place_id"));

            // Rating (if available)
            if (properties.has("rating")) {
                hotel.setRating(properties.get("rating").asDouble());
            }

            // Coordinates
            JsonNode geometry = feature.get("geometry");
            if (geometry != null && geometry.has("coordinates")) {
                JsonNode coords = geometry.get("coordinates");
                hotel.setLongitude(coords.get(0).asDouble());
                hotel.setLatitude(coords.get(1).asDouble());
            }

            // For now, we set priceLevel and openNow as default (Geoapify doesn't provide them directly)
            hotel.setPriceLevel(2);   // moderate by default
            hotel.setOpenNow(true);   // optimistic default

            // Photo (Geoapify sometimes provides preview)
            if (properties.has("preview")) {
                hotel.setPhotoUrl(properties.get("preview").asText());
            }

            return hotel;

        } catch (Exception e) {
            System.err.println("Failed to parse one hotel result");
            return null;
        }
    }

    private String getTextSafe(JsonNode node, String field) {
        if (node == null || !node.has(field)) return "";
        return node.get(field).asText("");
    }
}