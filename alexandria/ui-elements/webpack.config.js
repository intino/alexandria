const path = require("path");
const HtmlWebPackPlugin = require("html-webpack-plugin");
const CopyWebpackPlugin = require('copy-webpack-plugin');

module.exports = {
    module: {
        rules: [
            {
                test: /\.js$/,
                loader: "babel-loader",
                options: { rootMode: "upward" }
            },
            {
                test: /\.html$/,
                loader: "html-loader"
            },
            {
                test: /\.css$/,
                loader: 'style-loader!css-loader'
            }
        ]
    },
    entry : './gen/App.js',
    output: {
        path: "/Users/mcaballero/Proyectos/konos/out/production/ui-elements/www/ui-elements",
        publicPath: '/ui-elements/',
        filename: 'App.js'/*,
        chunkFilename: '[name].bundle.js'*/
    },
    resolve: {
        alias: {
            "alexandria-ui-elements": path.resolve(__dirname, '.'),
            "app-elements": path.resolve(__dirname, '.')
        }
    },
    plugins: [
        new HtmlWebPackPlugin({
            hash: true,
            title: "Test UI",
            template: "./home.html",
            filename: "./home.html"
        }),
        new HtmlWebPackPlugin({
            hash: true,
            title: "Test UI",
            template: "./docs.html",
            filename: "./docs.html"
        }),
        new HtmlWebPackPlugin({
            hash: true,
            title: "Test UI",
            template: "./widgetType.html",
            filename: "./widgetType.html"
        }),
        new CopyWebpackPlugin([{
            from: 'res',
            to: './res'
        }])
    ]
};