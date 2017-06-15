package com.netcracker.project.study.vaadin.client.page;

import com.vaadin.tapio.googlemaps.GoogleMap;
import com.vaadin.tapio.googlemaps.client.events.MarkerClickListener;
import com.vaadin.tapio.googlemaps.client.overlays.GoogleMapInfoWindow;
import com.vaadin.tapio.googlemaps.client.overlays.GoogleMapMarker;

public class OpenInfoWindowOnMarkerClickListener implements MarkerClickListener {

    private static final long serialVersionUID = 646386541641L;

    private final GoogleMap map;
    private final GoogleMapMarker marker;
    private final GoogleMapInfoWindow window;

    public OpenInfoWindowOnMarkerClickListener(GoogleMap map,
                                               GoogleMapMarker marker, GoogleMapInfoWindow window) {
        this.map = map;
        this.marker = marker;
        this.window = window;
    }

    @Override
    public void markerClicked(GoogleMapMarker clickedMarker) {
        if (clickedMarker.equals(marker)) {
            map.openInfoWindow(window);
        }
    }

}