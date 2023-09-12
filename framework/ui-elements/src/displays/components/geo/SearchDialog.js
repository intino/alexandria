import React from "react";
import IconButton from '@material-ui/core/IconButton';
import Search from '@material-ui/icons/Search';
import I18nComponent from "../../I18nComponent";

const SearchDialogStyles = {
	searchDialogTrigger : {
	    position: 'absolute',
	    right: '0',
	    marginTop: '10px',
	    zIndex: '1',
	    background: 'white',
	    padding: '9px',
	    border: '0',
	    borderRadius: '2px',
	    cursor: 'pointer'
	},
	searchDialog : {
	    position: 'absolute',
	    right: '0',
	    marginTop: '10px',
	    zIndex: '1',
	    background: 'white',
	    padding: '10px',
	    border: '0',
	    borderRadius: '2px',
	}
};

export default class SearchDialog extends I18nComponent {

    constructor(props) {
        super(props);
	    this.geocoder = (typeof google != "undefined") ? new google.maps.Geocoder() : null;
        this.state = {
            opened: false,
            search: { address: '', error: null }
        };
    };

    render = () => {
	    return (
	        <React.Fragment>
    	        {this.renderDialogTrigger()}
	            {this.renderDialog()}
	        </React.Fragment>
        );
    };

    renderDialogTrigger = () => {
        return (
            <IconButton style={this.searchDialogTriggerStyle()}
                aria-label={this.translate("Open search dialog")}
                onClick={this.handleOpenDialog.bind(this)}>
                <Search />
            </IconButton>
        );
    };

    handleOpenDialog = () => {
        this.setState({opened: true});
    };

    handleCloseDialog = () => {
        this.setState({opened: false});
    };

    renderDialog = () => {
        if (!this.state.opened) return;
        return (
            <form onSubmit={this.updateCoordinates.bind(this)} className="layout horizontal" style={this.searchDialogStyle()}>
                <div className="form-group layout vertical flex">
                    <input type="text" className="form-control" id="address" required aria-describedby="addressHelp"
                        value={this.state.search.address}
                        placeholder={this.translate("Address to search")}
                        onChange={this.setAddress.bind(this)}
                    />
                    {this.state.search.error && <div style={{color:'red',marginTop:'3px',fontSize:'7pt'}}>{this.state.search.error}</div>}
                </div>
                <button className="btn mb-4 btn-primary layout horizontal end-justified center" type='submit' style={{marginLeft:'10px'}}>{this.translate("Search")}</button>
                <button className="btn mb-4 btn-primary layout horizontal end-justified center" style={{marginLeft:'10px'}} onClick={this.handleCloseDialog.bind(this)}>{this.translate("Close")}</button>
            </form>
        );
    };

	searchDialogTriggerStyle = () => {
	    const result = { ...SearchDialogStyles.searchDialogTrigger };
	    result.marginRight = (this.props.mapOptions.fullscreenControl ? 60 : 10) + "px";
	    return result;
	};

	searchDialogStyle = () => {
	    const result = { ...SearchDialogStyles.searchDialog };
	    result.marginRight = (this.props.mapOptions.fullscreenControl ? 60 : 10) + "px";
	    return result;
	};

	updateCoordinates = (e) => {
	    e.preventDefault();
	    if (this.geocoder == null || this.state.search.address == "") return;
        const encodedAddress = encodeURI(this.state.search.address);
        this.geocoder.geocode({address: this.state.search.address, language: this.language()}, this.handleCoordinatesArrival.bind(this));
	};

	handleCoordinatesArrival = (result) => {
	    const map = this.props.map();
        if (map == null || !result || result.length < 1) {
            const search = this.state.search;
            search.error = this.translate("Address not found");
            this.setState({search});
            return;
        }
        map.setZoom(19);
        map.panTo(result[0].geometry.location);
    };

	setAddress = (e) => {
	    this.setState({search: { address: e.target.value, error: null }});
	};

}