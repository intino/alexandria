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
    entry : {
        'home' : './gen/home.js',
        'docs' : './gen/docs.js',
        'widgetType' : './gen/widgetType.js',
    },
    output: {
        path: "/Users/mcaballero/Proyectos/alexandria/out/production/ui-elements/www/ui-elements",
        publicPath: '/ui-elements/',
        filename: '[name].js'/*,
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
            chunks: ['home'],
            template: "./home.html",
            filename: "./home.html"
        }),
        new HtmlWebPackPlugin({
            hash: true,
            title: "Test UI",
            chunks: ['docs'],
            template: "./docs.html",
            filename: "./docs.html"
        }),
        new HtmlWebPackPlugin({
            hash: true,
            title: "Test UI",
            chunks: ['widgetType'],
            template: "./widgetType.html",
            filename: "./widgetType.html"
        }),
        new CopyWebpackPlugin([{
            from: 'res',
            to: './res'
        }])
    ]
};