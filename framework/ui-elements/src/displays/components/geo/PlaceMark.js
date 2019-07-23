import React from "react";
import { Marker, Polygon, Polyline, InfoWindow } from '@react-google-maps/api'
import I18nComponent from "../../I18nComponent";
import DisplayFactory from "alexandria-ui-elements/src/displays/DisplayFactory";

export default class PlaceMark extends I18nComponent {

    state = {
        isOpen: this.props.showInfo
    };

    constructor(props) {
        super(props);
    };

    render = () => {
        const placeMark = this.props.placeMark;
        const location = placeMark.location;
        const clusterer = this.props.clusterer;
        var icon = {url:placeMark.icon != null ? placeMark.icon : (this.props.icon != null ? this.props.icon : undefined),labelOrigin: new google.maps.Point(9, 10)};
        if (icon.url === undefined) icon = undefined;
        if (location.type === "Polyline") return (<Marker icon={icon} label={{text:placeMark.label}} position={this.centerOf(placeMark)} onClick={this.showInfo.bind(this)}><Polyline path={location.pointList} clusterer={clusterer}></Polyline>{this.renderInfoWindow()}</Marker>);
        else if (location.type === "Polygon") return (<Marker icon={icon} label={{text:placeMark.label}} position={this.centerOf(placeMark)} onClick={this.showInfo.bind(this)}><Polygon path={location.pointList} clusterer={clusterer}></Polygon>{this.renderInfoWindow()}</Marker>);
        return (<Marker icon={icon} label={{text:placeMark.label}} position={location.pointList[0]} clusterer={clusterer} onClick={this.showInfo.bind(this)}>{this.renderInfoWindow()}</Marker>);
    };

    renderInfoWindow = () => {
        if (this.props.content == null || this.state.isOpen == null || !this.state.isOpen) return null;
        const placeMark = this.props.placeMark;
        const pos = placeMark.pos;
        const content = this.props.content;
        return (
            <InfoWindow key={"_info" + pos} visible={true} position={this.centerOf(placeMark)} onCloseClick={this.hideInfo.bind(this)}>
                <div>{content != null ? React.createElement(DisplayFactory.get(content.tp), content.pl) : "Loading"}</div>
            </InfoWindow>
        );
    };

    showLoading = () => {
        this.setState({ isOpen: true });
    };

    showInfo = () => {
        if (this.props.onShowInfo != null) this.props.onShowInfo(this);
        else this.setState({ isOpen: !this.state.isOpen });
    };

    isInfoVisible = () => {
        return this.state.isOpen;
    };

    hideInfo = () => {
        this.setState({ isOpen: false });
    };

    centerOf = (placeMark) => {
        const type = placeMark.location.type;
        if (placeMark.location.type === "Polyline" || type === "Polygon") return this.calculateCenter(placeMark.location.pointList);
        return new google.maps.LatLng(placeMark.location.pointList[0].lat, placeMark.location.pointList[0].lng);
    };

    calculateCenter = (vertices) => {
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
    };

}