import React from "react";
import { Marker, Polygon, Polyline, InfoWindow } from '@react-google-maps/api'
import I18nComponent from "../../I18nComponent";
import DisplayFactory from "alexandria-ui-elements/src/displays/DisplayFactory";
import GeometryUtil from "../../../util/GeometryUtil";

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
        const googleMapStructure = GeometryUtil.toGoogleMapStructure(location);
        const clusterer = this.props.clusterer;
        var icon = {url:placeMark.icon != null ? placeMark.icon : (this.props.icon != null ? this.props.icon : undefined),labelOrigin: new google.maps.Point(9, 10)};
        if (icon.url === undefined) icon = undefined;
        if (location.type === "Polyline") return (<Marker icon={icon} label={{text:placeMark.label}} position={GeometryUtil.centerOf(placeMark.location)} onClick={this.showInfo.bind(this)}><Polyline path={googleMapStructure} clusterer={clusterer}/>{this.renderInfoWindow()}</Marker>);
        else if (location.type === "Polygon") return (<Marker icon={icon} label={{text:placeMark.label}} position={GeometryUtil.centerOf(placeMark.location)} onClick={this.showInfo.bind(this)}><Polygon paths={googleMapStructure} clusterer={clusterer}/>{this.renderInfoWindow()}</Marker>);
        return (<Marker icon={icon} label={{text:placeMark.label}} position={googleMapStructure} clusterer={clusterer} onClick={this.showInfo.bind(this)}>{this.renderInfoWindow()}</Marker>);
    };

    renderInfoWindow = () => {
        if (this.props.content == null || this.state.isOpen == null || !this.state.isOpen) return null;
        const placeMark = this.props.placeMark;
        const pos = placeMark.pos;
        const content = this.props.content;
        return (
            <InfoWindow key={"_info" + pos} visible={true} position={GeometryUtil.centerOf(placeMark.location)} onCloseClick={this.hideInfo.bind(this)}>
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

}