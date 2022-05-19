import './TopBar.css'
import React from 'react'
import PropTypes from 'prop-types'

import { parseSearch, serializeSearch } from '../utils'

export default class Topbar extends React.Component {

    static propTypes = {
        layoutActions: PropTypes.object.isRequired
    }

    constructor(props, context) {
        super(props, context)
        this.state = { url: props.specSelectors.url(), selectedIndex: 0 }
    }

    componentWillReceiveProps(nextProps) {
        this.setState({ url: nextProps.specSelectors.url() })
    }

    onUrlChange = (e) => {
        let { target: { value } } = e
        this.setState({ url: value })
    }

    loadSpec = (url) => {
        this.props.specActions.updateUrl(url)
        this.props.specActions.download(url)
    }

    onUrlSelect = (e) => {
        let url = e.target.value || e.target.href
        this.loadSpec(url)
        this.setSelectedUrl(url)
        e.preventDefault()
    }

    setSearch = (spec) => {
        let search = parseSearch()
        search["urls.primaryName"] = spec.name
        const newUrl = `${window.location.protocol}//${window.location.host}${window.location.pathname}`
        if (window && window.history && window.history.pushState) {
            window.history.replaceState(null, "", `${newUrl}?${serializeSearch(search)}`)
        }
    }

    setSelectedUrl = (selectedUrl) => {
        const configs = this.props.getConfigs()
        const urls = configs.urls || []

        if (urls && urls.length) {
            if (selectedUrl) {
                urls.forEach((spec, i) => {
                    if (spec.url === selectedUrl) {
                        this.setState({ selectedIndex: i })
                        this.setSearch(spec)
                    }
                })
            }
        }
    }

    componentWillMount() {
        const configs = this.props.getConfigs()
        const urls = configs.urls || []

        if (urls && urls.length) {
            let primaryName = configs["urls.primaryName"]
            if (primaryName) {
                urls.forEach((spec, i) => {
                    if (spec.name === primaryName) {
                        this.setState({ selectedIndex: i })
                    }
                })
            }
        }
    }

    componentDidMount() {
        const urls = this.props.getConfigs().urls || []

        if (urls && urls.length) {
            this.loadSpec(urls[this.state.selectedIndex].url)
        }
    }

    onFilterChange = (e) => {
        let { target: { value } } = e
        this.props.layoutActions.updateFilter(value)
    }

    _renderOptions(urls) {
        return urls.map((link, i) => (<option key={i} value={link.url}>{link.name}</option>));
    }

    render() {
        let { specSelectors, getConfigs } = this.props;

        let isLoading = specSelectors.loadingStatus() === "loading"

        const { urls, custom } = getConfigs();
        const { title, subtitle, color, background, selectorBorder } = custom || {};

        return (
            <div className="topbar" style={{ background: background, color: color }}>
                <div className="wrapper">
                    <div className="topbar-wrapper">
                        <div className="topbar-title">
                            <img src="./images/logo.png" alt={title} />
                            <div className="topbar-title-info">
                                <h1>{title}</h1>
                                <span>{subtitle}</span>
                            </div>
                        </div>
                        {
                            urls &&
                            urls.length > 1 &&
                            (
                                <form className="topbar-selector">
                                    <label className="select-label" htmlFor="select">
                                        <select id="select" disabled={isLoading} onChange={this.onUrlSelect} value={urls[this.state.selectedIndex].url} style={{ borderColor: selectorBorder }}>
                                            {this._renderOptions(urls)}
                                        </select>
                                    </label>
                                </form>
                            )
                        }
                    </div>
                </div>
            </div>
        )
    }
}

Topbar.propTypes = {
    specSelectors: PropTypes.object.isRequired,
    specActions: PropTypes.object.isRequired,
    getComponent: PropTypes.func.isRequired,
    getConfigs: PropTypes.func.isRequired
}