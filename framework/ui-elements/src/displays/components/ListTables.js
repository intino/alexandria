import React from "react";
import { withStyles } from '@material-ui/core/styles';
import Table from '@material-ui/core/Table';
import TablePagination from '@material-ui/core/TablePagination';
import TableBody from '@material-ui/core/TableBody';
import TableRow from '@material-ui/core/TableRow';
import TableCell from '@material-ui/core/TableCell';
import AbstractList from "../../../gen/displays/components/AbstractList";
import ListNotifier from "../../../gen/displays/notifiers/ListNotifier";
import ListRequester from "../../../gen/displays/requesters/ListRequester";
import DisplayFactory from "alexandria-ui-elements/src/display/DisplayFactory";

const styles = theme => ({
	list: { minHeight:'200px',height:'100%',width:'100%' },
	item: { border:'0' }
});

class List extends AbstractList {
	state = {
		itemsPerPage: 10,
		countItems : 100,
		page: 0
	};

	constructor(props) {
		super(props);
		this.notifier = new ListNotifier(this);
		this.requester = new ListRequester(this);
		this.container = React.createRef();
	};

	render() {
		const { classes } = this.props;
		const { itemsPerPage, page, countItems } = this.state;
		const items = this.instances();
		const emptyItems = itemsPerPage - Math.min(itemsPerPage, items.length - page * itemsPerPage);

		return (
			<div>
				<Table className={classes.list} height="100%" width="100%">
					<TableBody>
						{items.slice(page * itemsPerPage, page * itemsPerPage + itemsPerPage).map((item, i) => this.renderItem(item, i))}
						{emptyItems > 0 && (<TableRow style={{ height: 48 * emptyItems }}><TableCell/></TableRow>)}
					</TableBody>
				</Table>
				<TablePagination rowsPerPageOptions={[5, 10, 25]} component="div" count={countItems}
					rowsPerPage={itemsPerPage} page={page}
					backIconButtonProps={{
						'aria-label': 'Previous Page',
					}}
					nextIconButtonProps={{
						'aria-label': 'Next Page',
					}}
					onChangePage={this.handleChangePage}
					onChangeRowsPerPage={this.handleChangeItemsPerPage}
				/>
			</div>
		);
	};

	handleChangePage = (e, page) => {
		this.page(page);
		// this.setState({ page : page });
	};

	handleChangeItemsPerPage = event => {
		this.itemsPerPage(event.target.value);
		// this.setState({ itemsPerPage: event.target.value });
	};

	renderItem(definition, index) {
		const { classes } = this.props;
		const instance = React.createElement(DisplayFactory.get(definition.tp), definition.pl);
		return (
			<TableRow key={index} className={classes.item}>
				<TableCell className={classes.item} component="th" scope="row">{instance}</TableCell>
			</TableRow>
		);
	};

	componentDidUpdate = () => {
		this.requester.notifyItemsRendered();
	};
}

export default withStyles(styles, { withTheme: true })(List);