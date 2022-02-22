import { createBrowserHistory } from 'history';

const history = createBrowserHistory();
history.continueListening = () => {
    if (history.onContinueListening) history.onContinueListening();
};
history.stopListening = () => {
    if (history.onStopListening) history.onStopListening();
};
export default history;