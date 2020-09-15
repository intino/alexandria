const GeometryUtil = (function () {

    function _toPaths(googleMapPaths) {
        return googleMapPaths.getArray().map((path) => _toPath(path));
    }

    function _toPath(googleMapPath) {
        return { pointList: _toPoints(googleMapPath.getArray()) };
    }

    function _toPoints(pointList) {
        return pointList.map((point) => _toPoint(point));
    }

    function _toPoint(googleMapMarker) {
        return { lat: googleMapMarker.lat(), lng: googleMapMarker.lng() };
    }

    function calculateCenter(vertices) {
        var latitudes = [];
        var longitudes = [];

        for (var i = 0; i < vertices.length; i++) {
            longitudes.push(vertices[i].lng);
            latitudes.push(vertices[i].lat);
        }

        latitudes.sort();
        longitudes.sort();

        var lowX = latitudes[0];
        var highX = latitudes[latitudes.length - 1];
        var lowy = longitudes[0];
        var highy = longitudes[latitudes.length - 1];

        var centerX = lowX + ((highX - lowX) / 2);
        var centerY = lowy + ((highy - lowy) / 2);

        return (new google.maps.LatLng(centerX, centerY));
    }

    return {
        toGoogleMapStructure: (geometry) => {
            if (geometry.type === "Point") return geometry.point;
            else if (geometry.type === "Polyline") return geometry.path.pointList;
            else if (geometry.type === "Polygon") {
                const paths = geometry.paths;
                let result = [];
                for (let i = 0; i < paths.length; i++) {
                    result.push(paths[i].pointList);
                }
                return result;
            }
            return null;
        },
        firstPoint: (geometry) => {
            let lat = -1000, lng = -1000;
            if (geometry.type === "Point" && geometry.point != null) { lat = geometry.point.lat; lng = geometry.point.lng }
            else if (geometry.type === "Polyline" && geometry.path != null) { lat = geometry.path.pointList[0].lat; lng = geometry.path.pointList[0].lng }
            else if (geometry.type === "Polygon" && geometry.paths.length > 0) { lat = geometry.paths[0].pointList[0].lat; lng = geometry.paths[0].pointList[0].lng }
            return new google.maps.LatLng(lat, lng);
        },
        rectangleToPolygon: (googleMapsRectangle) => {
            var bounds = googleMapsRectangle.getBounds();
            var NE = bounds.getNorthEast();
            var SW = bounds.getSouthWest();
            var NW = new google.maps.LatLng(NE.lat(), SW.lng());
            var SE = new google.maps.LatLng(SW.lat(), NE.lng());
            return {type: 'Polygon', paths: [{ pointList : _toPoints([NW, NE, SE, SW, NW]) }]};
        },
        toPolygon: (googleMapsPolygon) => {
            const paths = _toPaths(googleMapsPolygon.getPaths());
            paths.map((path) => path.pointList.push(path.pointList[0]));
            return {type: 'Polygon', paths: paths };
        },
        toPolyline: (googleMapsPolygon) => {
            const path = _toPath(googleMapsPolygon.getPath());
            return {type: 'Polyline', path: path};
        },
        toPoint: (googleMapsMarker) => {
            const point = _toPoint(googleMapsMarker.getPosition());
            return {type: 'Point', point: point};
        },
        centerOf: (location) => {
            const type = location.type;
            if (type === "Polyline") return calculateCenter(location.path.pointList);
            else if (type === "Polygon") return calculateCenter(location.paths[0].pointList);
            return new google.maps.LatLng(location.point.lat, location.point.lng);
        }
    }
})();

export default GeometryUtil;