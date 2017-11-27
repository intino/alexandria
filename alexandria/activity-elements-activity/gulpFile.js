'use strict';

// libraries
const gulp = require('gulp');
const $ = require('gulp-load-plugins')();
const del = require('del');
const runSequence = require('run-sequence');
const merge = require('merge-stream');
const path = require('path');
const concat = require('gulp-concat');
const eventStream = require('event-stream');
const console = require('gulp-util');
const os = require('os');
const OutputPath = '/Users/oroncal/workspace/konos/out/production/activity-elements-activity/www/activity-elements';
const WorkingDirectory = '/Users/oroncal/workspace/konos/alexandria/activity-elements-activity';
const WatchPort = 35638;

// root tasks
gulp.task('default', cb => {
	runSequence('clean', 'package', 'install', cb);
});

gulp.task('dev', cb => {
	runSequence('default', 'watch', cb);
});

gulp.task('deploy', cb => {
	runSequence('clean', 'package', 'obfuscate', 'install', cb);
});

// sub tasks
gulp.task('clean', () => {return del([packagePath()], {force:true}); });

gulp.task('package', cb => {
	runSequence('compile', 'clean-compile', 'union', cb);
});

gulp.task('compile', cb => {
	runSequence('prepare-compile', 'do-compile', cb);
});

gulp.task('prepare-compile', cb => {
	return eventStream.concat(
		gulp.src(workingPath('src/**/*')).pipe(gulp.dest(packagePath("/"))),
		gulp.src(workingPath('lib/**/*')).pipe(gulp.dest(packagePath("/lib")))
	);
});

gulp.task('do-compile', cb => {
	return eventStream.concat(
		compileFile(packagePath(''), '/components.html')
	);
});

gulp.task('clean-compile', cb => {
	return del([packagePath('/components'), packagePath('/widgets'), packagePath('/widgets.html'), packagePath('/lib')], {force:true});
});

gulp.task('union', cb => {
	return eventStream.concat(
		unionFonts(workingPath('src/fonts/**/*'), packagePath("/fonts")),
		unionStyles(workingPath('src/styles/**/*'), packagePath("/styles")),
		gulp.src(workingPath('src/styles/**/*')).pipe(gulp.dest(packagePath("/styles"))),
		unionImages(workingPath('src/images/**/*'), packagePath("/images")),
		gulp.src([workingPath('src/*.{html,js}'), '!' + workingPath('src/components.html'), '!' + workingPath('**/.DS_Store')]).pipe(gulp.dest(packagePath(''))),
		unionLib()
	);
});

gulp.task('install', ['resolve-url-dependencies'],() => {
	del([outputPath()], {force:true});

	return eventStream.concat(
		gulp.src([packagePath('/**/*')], { dot: true }).pipe(gulp.dest(outputPath()))
	);
});

gulp.task('resolve-url-dependencies', () => {
	return gulp.src([packagePath('*.html'), '!' + packagePath('/components.html')])
		.pipe($.replace('src="', 'src="$url/'))
		.pipe($.replace('href="', 'href="$url/'))
		.pipe($.replace('href-absolute=', 'href='))
		.pipe(gulp.dest(packagePath()));
});

gulp.task('watch', () => {
	const tasksToLaunch = ['refresh-dev-server'];

	gulp.watch([workingPath('src/**/*.html')], tasksToLaunch);
	gulp.watch([workingPath('src/styles/**/*.css')], tasksToLaunch);
	gulp.watch([workingPath('src/widgets/**/*.css')], tasksToLaunch);
	gulp.watch([workingPath('src/widgets/**/*.html')], tasksToLaunch);
	gulp.watch([workingPath('src/components/**/*.css')], tasksToLaunch);
	gulp.watch([workingPath('src/components/**/*.html')], tasksToLaunch);
	gulp.watch([workingPath('src/images/**/*')], tasksToLaunch);

	$.livereload.listen({ port: WatchPort, basePath: '.' });
});

gulp.task('refresh-dev-server', ['default', 'refresh-browser']);
gulp.task('refresh-browser', () => gulp.src(outputPath('/*.html')).pipe($.livereload()));

gulp.task('obfuscate', () => {
	return eventStream.concat(
		obfuscateFile(packagePath('/components.html'))
	);
});

// constants
const PackageDirectory = '/Users/oroncal/workspace/konos/alexandria/activity-elements-activity/dist';

const packagePath = subpath => !subpath ? PackageDirectory : path.join(PackageDirectory, subpath);
const outputPath = subpath => !subpath ? OutputPath : path.join(OutputPath, subpath);
const workingPath = subpath => !subpath ? WorkingDirectory : path.join(WorkingDirectory, subpath);

const compileFile = (path, src) => {
    var isWin = (os.type().toLowerCase().startsWith() == "win");
    var vulcanizeOptions = { stripComments: true, inlineCss: true, inlineScripts: true };

    if (isWin) vulcanizeOptions.abspath = path;
    else src = path + src;

    return gulp.src(src)
        .pipe($.vulcanize(vulcanizeOptions))
        .pipe($.replace('<iron-a11y-keys target="{{}}" keys="space enter" on-keys-pressed="toggleOpened"><\/iron-a11y-keys>', '<!-- commented by sumus to fix important events bug <iron-a11y-keys target="{{}}" keys="space enter" on-keys-pressed="toggleOpened"><\/iron-a11y-keys>-->')) // fix important paper-chip bug
        .pipe(gulp.dest(packagePath()))
        .pipe($.size({title: 'vulcanize'}));
};

const obfuscateFile = (src) => {
	return gulp.src(src)
		.pipe($.htmlmin({
			collapseWhitespace: true,
			removeComments: true,
			removeTagWhitespace: true,
			minifyCSS: true,
			minifyJS: true,
			trimCustomFragments : true
		}))
		.pipe(gulp.dest(packagePath()));
};

const unionLib = () => {
	return gulp.src([workingPath('lib/{jquery,webcomponentsjs,moment,konos-server-web,numeral,promise-polyfill}/**/*'),
		workingPath('lib/{cotton-cookies,cotton-push}/*.js')])
		.pipe(gulp.dest(packagePath('lib')));
};

const unionStyles = (src, dest) => {
	return gulp.src(src)
		.pipe($.changed(src, {extension: '.html'}))
		.pipe($.changed(src, {extension: '.css'}))
		.pipe(gulp.dest(dest))
		.pipe(gulp.dest(dest))
		.pipe($.size({title: src}));
};

const unionImages = (src, dest) => {
	return gulp.src(src)
		.pipe(gulp.dest(dest))
		.pipe($.size({title: 'images'}));
};

const unionFonts = (src, dest) => {
	return gulp.src(src)
		.pipe(gulp.dest(dest))
		.pipe($.size({title: 'fonts'}));
};

require('web-component-tester').gulp.init(gulp);