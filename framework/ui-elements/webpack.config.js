const path = require("path");
const HtmlWebPackPlugin = require("html-webpack-plugin");
const CopyWebpackPlugin = require('copy-webpack-plugin');
const CircularDependencyPlugin = require('circular-dependency-plugin');

module.exports = {
    module: {
        rules: [
            {
                test: /\.js$/,
                exclude: /(node_modules)/,
                options: {
                    rootMode: "upward",
                    presets: ['@babel/preset-env'],
                    cacheDirectory: true
                },
                loader: "babel-loader"
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
        path: "/Users/mcaballero/Proyectos/alexandria/out/production/ui-elements/www/ui-elements",
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
        new CircularDependencyPlugin({
            failOnError: true,
            allowAsyncCycles: false,
            cwd: process.cwd(),
        }),
        new HtmlWebPackPlugin({
            hash: true,
            title: "Test UI",
            template: "./home.html",
            filename: "./home.html"
        }),
        new CopyWebpackPlugin([{
            from: 'res',
            to: './res'
        }])
    ]
};