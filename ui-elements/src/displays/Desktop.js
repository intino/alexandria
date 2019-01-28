import PageDisplay from './PageDisplay'
import AppBar from './components/AppBar'
import Tabs from './components/Tabs'

export default class Desktop extends PageDisplay {

    constructor(props) {
        super(props);
    };

    render() {
        <React.Fragment>
            <AppBar></AppBar>
            <Tabs></Tabs>
        </React.Fragment>
    }
}