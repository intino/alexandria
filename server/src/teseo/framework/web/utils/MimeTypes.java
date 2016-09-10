/*
    Monet will assist business to process re-engineering. Monet separate the
    business logic from the underlying technology to allow Model-Driven
    Engineering (MDE). These models guide all the development process over a
    Service Oriented Architecture (SOA).

    Copyright (C) 2009  Grupo de Ingenieria del Sofware y Sistemas de la Universidad de Las Palmas de Gran Canaria

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see http://www.gnu.org/licenses/.
 */

package teseo.framework.web.utils;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.Arrays.asList;

public class MimeTypes {
	private static final Map<String, String> mimeTypes = new HashMap<>();
	private static final Map<String, String> mimeTypesInverted = new HashMap<>();

	public static final String DEFAULT_MIME_TYPE = "bin";
	public static final String DEFAULT_CONTENT_TYPE = "application/octet-stream";

	public static final String TEXT = "text/plain";
	public static final String XML = "text/xml";
	public static final String PDF = "application/pdf";

	private static final String DEFAULT_EXTENSION = "bin";

	private static final List<String> PREVIEWABLE_TYPES = asList("application/vnd.oasis.opendocument.text",
			"application/pdf", "application/msword",
			"application/vnd.openxmlformats-officedocument.wordprocessingml.document");

	static {
		registerMimeTypes();
	}

	public static String get(String code) {
		return mimeTypes.getOrDefault(code, mimeTypes.get(MimeTypes.DEFAULT_MIME_TYPE));
	}

	public static String getExtension(String mimeType) {
		return mimeTypesInverted.getOrDefault(mimeType, MimeTypes.DEFAULT_EXTENSION);
	}

	public static boolean isPreviewable(String contentType) {
		return PREVIEWABLE_TYPES.contains(contentType) || contentType.startsWith("image");
	}

	public static String getFromStream(InputStream stream) {
		return Files.getContentType(stream);
	}

	public static String getFromFile(File file) {
		return getFromFilename(file.getAbsolutePath());
	}

	public static String getFromFilename(String filename) {
		return getContentType(filename, Files.getExtension(filename));
	}

	private static String getContentType(String filename, Optional<String> extension) {
		String contentType = null;

		if (extension.isPresent() && mimeTypes.containsKey(extension.get()))
			contentType = mimeTypes.get(extension.get());

		if (contentType == null || contentType.equals(DEFAULT_CONTENT_TYPE))
			return Files.getContentType(new File(filename));

		return contentType;
	}

	public static boolean isImage(String contentType) {
		return contentType.contains("image");
	}

	private static void registerMimeTypes() {
		mimeTypes.put("323", "text/h323");
		mimeTypes.put("3gp", "video/3gpp");
		mimeTypes.put("7z", "application/x-7z-compressed");
		mimeTypes.put("abw", "application/x-abiword");
		mimeTypes.put("ai", "application/postscript");
		mimeTypes.put("aif", "audio/x-aiff");
		mimeTypes.put("aifc", "audio/x-aiff");
		mimeTypes.put("aiff", "audio/x-aiff");
		mimeTypes.put("alc", "chemical/x-alchemy");
		mimeTypes.put("art", "image/x-jg");
		mimeTypes.put("asc", "text/plain");
		mimeTypes.put("asf", "video/x-ms-asf");
		mimeTypes.put("asn", "chemical/x-ncbi-asn1");
		mimeTypes.put("asn", "chemical/x-ncbi-asn1-spec");
		mimeTypes.put("aso", "chemical/x-ncbi-asn1-binary");
		mimeTypes.put("asx", "video/x-ms-asf");
		mimeTypes.put("atom", "application/atom");
		mimeTypes.put("atomcat", "application/atomcat+xml");
		mimeTypes.put("atomsrv", "application/atomserv+xml");
		mimeTypes.put("au", "audio/basic");
		mimeTypes.put("avi", "video/x-msvideo");
		mimeTypes.put("bak", "application/x-trash");
		mimeTypes.put("bat", "application/x-msdos-program");
		mimeTypes.put("b", "chemical/x-molconn-Z");
		mimeTypes.put("bcpio", "application/x-bcpio");
		mimeTypes.put("bib", "text/x-bibtex");
		mimeTypes.put("bin", "application/octet-stream");
		mimeTypes.put("bmp", "image/x-ms-bmp");
		mimeTypes.put("book", "application/x-maker");
		mimeTypes.put("boo", "text/x-boo");
		mimeTypes.put("bsd", "chemical/x-crossfire");
		mimeTypes.put("c3d", "chemical/x-chem3d");
		mimeTypes.put("cab", "application/x-cab");
		mimeTypes.put("cac", "chemical/x-cache");
		mimeTypes.put("cache", "chemical/x-cache");
		mimeTypes.put("cap", "application/cap");
		mimeTypes.put("cascii", "chemical/x-cactvs-binary");
		mimeTypes.put("cat", "application/vnd.ms-pki.seccat");
		mimeTypes.put("cbin", "chemical/x-cactvs-binary");
		mimeTypes.put("cbr", "application/x-cbr");
		mimeTypes.put("cbz", "application/x-cbz");
		mimeTypes.put("cc", "text/x-c++src");
		mimeTypes.put("cdf", "application/x-cdf");
		mimeTypes.put("cdr", "image/x-coreldraw");
		mimeTypes.put("cdt", "image/x-coreldrawtemplate");
		mimeTypes.put("cdx", "chemical/x-cdx");
		mimeTypes.put("cdy", "application/vnd.cinderella");
		mimeTypes.put("cef", "chemical/x-cxf");
		mimeTypes.put("cer", "chemical/x-cerius");
		mimeTypes.put("chm", "chemical/x-chemdraw");
		mimeTypes.put("chrt", "application/x-kchart");
		mimeTypes.put("cif", "chemical/x-cif");
		mimeTypes.put("class", "application/java-vm");
		mimeTypes.put("cls", "text/x-tex");
		mimeTypes.put("cmdf", "chemical/x-cmdf");
		mimeTypes.put("cml", "chemical/x-cml");
		mimeTypes.put("cod", "application/vnd.rim.cod");
		mimeTypes.put("com", "application/x-msdos-program");
		mimeTypes.put("cpa", "chemical/x-compass");
		mimeTypes.put("cpio", "application/x-cpio");
		mimeTypes.put("cpp", "text/x-c++src");
		mimeTypes.put("cpt", "application/mac-compactpro");
		mimeTypes.put("cpt", "image/x-corelphotopaint");
		mimeTypes.put("crl", "application/x-pkcs7-crl");
		mimeTypes.put("crt", "application/x-x509-ca-cert");
		mimeTypes.put("csf", "chemical/x-cache-csf");
		mimeTypes.put("csh", "application/x-csh");
		mimeTypes.put("csh", "text/x-csh");
		mimeTypes.put("csm", "chemical/x-csml");
		mimeTypes.put("csml", "chemical/x-csml");
		mimeTypes.put("css", "text/css");
		mimeTypes.put("csv", "text/csv");
		mimeTypes.put("ctab", "chemical/x-cactvs-binary");
		mimeTypes.put("c", "text/x-csrc");
		mimeTypes.put("ctx", "chemical/x-ctx");
		mimeTypes.put("cu", "application/cu-seeme");
		mimeTypes.put("cub", "chemical/x-gaussian-cube");
		mimeTypes.put("cxf", "chemical/x-cxf");
		mimeTypes.put("cxx", "text/x-c++src");
		mimeTypes.put("dat", "chemical/x-mopac-input");
		mimeTypes.put("dcr", "application/x-director");
		mimeTypes.put("deb", "application/x-debian-package");
		mimeTypes.put("diff", "text/x-diff");
		mimeTypes.put("dif", "video/dv");
		mimeTypes.put("dir", "application/x-director");
		mimeTypes.put("djv", "image/vnd.djvu");
		mimeTypes.put("djvu", "image/vnd.djvu");
		mimeTypes.put("dll", "application/x-msdos-program");
		mimeTypes.put("dl", "video/dl");
		mimeTypes.put("dmg", "application/x-apple-diskimage");
		mimeTypes.put("dms", "application/x-dms");
		mimeTypes.put("doc", "application/msword");
		mimeTypes.put("dot", "application/msword");
		mimeTypes.put("d", "text/x-dsrc");
		mimeTypes.put("dvi", "application/x-dvi");
		mimeTypes.put("dv", "video/dv");
		mimeTypes.put("dx", "chemical/x-jcamp-dx");
		mimeTypes.put("dxr", "application/x-director");
		mimeTypes.put("emb", "chemical/x-embl-dl-nucleotide");
		mimeTypes.put("embl", "chemical/x-embl-dl-nucleotide");
		mimeTypes.put("eml", "message/rfc822");
		mimeTypes.put("ent", "chemical/x-ncbi-asn1-ascii");
		mimeTypes.put("ent", "chemical/x-pdb");
		mimeTypes.put("eps", "application/postscript");
		mimeTypes.put("etx", "text/x-setext");
		mimeTypes.put("exe", "application/x-msdos-program");
		mimeTypes.put("ez", "application/andrew-inset");
		mimeTypes.put("fb", "application/x-maker");
		mimeTypes.put("fbdoc", "application/x-maker");
		mimeTypes.put("fch", "chemical/x-gaussian-checkpoint");
		mimeTypes.put("fchk", "chemical/x-gaussian-checkpoint");
		mimeTypes.put("fig", "application/x-xfig");
		mimeTypes.put("flac", "application/x-flac");
		mimeTypes.put("fli", "video/fli");
		mimeTypes.put("fm", "application/x-maker");
		mimeTypes.put("frame", "application/x-maker");
		mimeTypes.put("frm", "application/x-maker");
		mimeTypes.put("gal", "chemical/x-gaussian-log");
		mimeTypes.put("gam", "chemical/x-gamess-input");
		mimeTypes.put("gamin", "chemical/x-gamess-input");
		mimeTypes.put("gau", "chemical/x-gaussian-input");
		mimeTypes.put("gcd", "text/x-pcs-gcd");
		mimeTypes.put("gcf", "application/x-graphing-calculator");
		mimeTypes.put("gcg", "chemical/x-gcg8-sequence");
		mimeTypes.put("gen", "chemical/x-genbank");
		mimeTypes.put("gf", "application/x-tex-gf");
		mimeTypes.put("gif", "image/gif");
		mimeTypes.put("gjc", "chemical/x-gaussian-input");
		mimeTypes.put("gjf", "chemical/x-gaussian-input");
		mimeTypes.put("gl", "video/gl");
		mimeTypes.put("gnumeric", "application/x-gnumeric");
		mimeTypes.put("gpt", "chemical/x-mopac-graph");
		mimeTypes.put("gsf", "application/x-font");
		mimeTypes.put("gsm", "audio/x-gsm");
		mimeTypes.put("gtar", "application/x-gtar");
		mimeTypes.put("hdf", "application/x-hdf");
		mimeTypes.put("hh", "text/x-c++hdr");
		mimeTypes.put("hin", "chemical/x-hin");
		mimeTypes.put("hpp", "text/x-c++hdr");
		mimeTypes.put("hqx", "application/mac-binhex40");
		mimeTypes.put("hs", "text/x-haskell");
		mimeTypes.put("hta", "application/hta");
		mimeTypes.put("htc", "text/x-component");
		mimeTypes.put("h", "text/x-chdr");
		mimeTypes.put("html", "text/html");
		mimeTypes.put("htm", "text/html");
		mimeTypes.put("hxx", "text/x-c++hdr");
		mimeTypes.put("ica", "application/x-ica");
		mimeTypes.put("ice", "x-conference/x-cooltalk");
		mimeTypes.put("ico", "image/x-icon");
		mimeTypes.put("ics", "text/calendar");
		mimeTypes.put("icz", "text/calendar");
		mimeTypes.put("ief", "image/ief");
		mimeTypes.put("iges", "model/iges");
		mimeTypes.put("igs", "model/iges");
		mimeTypes.put("iii", "application/x-iphone");
		mimeTypes.put("inp", "chemical/x-gamess-input");
		mimeTypes.put("ins", "application/x-internet-signup");
		mimeTypes.put("iso", "application/x-iso9660-image");
		mimeTypes.put("isp", "application/x-internet-signup");
		mimeTypes.put("ist", "chemical/x-isostar");
		mimeTypes.put("istr", "chemical/x-isostar");
		mimeTypes.put("jad", "text/vnd.sun.j2me.app-descriptor");
		mimeTypes.put("jar", "application/java-archive");
		mimeTypes.put("java", "text/x-java");
		mimeTypes.put("jdx", "chemical/x-jcamp-dx");
		mimeTypes.put("jmz", "application/x-jmol");
		mimeTypes.put("jng", "image/x-jng");
		mimeTypes.put("jnlp", "application/x-java-jnlp-file");
		mimeTypes.put("jpeg", "image/jpeg");
		mimeTypes.put("jpe", "image/jpeg");
		mimeTypes.put("jpg", "image/jpeg");
		mimeTypes.put("js", "application/x-javascript");
		mimeTypes.put("kar", "audio/midi");
		mimeTypes.put("key", "application/pgp-keys");
		mimeTypes.put("kil", "application/x-killustrator");
		mimeTypes.put("kin", "chemical/x-kinemage");
		mimeTypes.put("kml", "application/vnd.google-earth.kml+xml");
		mimeTypes.put("kmz", "application/vnd.google-earth.kmz");
		mimeTypes.put("kpr", "application/x-kpresenter");
		mimeTypes.put("kpt", "application/x-kpresenter");
		mimeTypes.put("ksp", "application/x-kspread");
		mimeTypes.put("kwd", "application/x-kword");
		mimeTypes.put("kwt", "application/x-kword");
		mimeTypes.put("latex", "application/x-latex");
		mimeTypes.put("lha", "application/x-lha");
		mimeTypes.put("lhs", "text/x-literate-haskell");
		mimeTypes.put("lsf", "video/x-la-asf");
		mimeTypes.put("lsx", "video/x-la-asf");
		mimeTypes.put("ltx", "text/x-tex");
		mimeTypes.put("lyx", "application/x-lyx");
		mimeTypes.put("lzh", "application/x-lzh");
		mimeTypes.put("lzx", "application/x-lzx");
		mimeTypes.put("m3u", "audio/mpegurl");
		mimeTypes.put("m3u", "audio/x-mpegurl");
		mimeTypes.put("m4a", "audio/mpeg");
		mimeTypes.put("m4a", "video/mp4");
		mimeTypes.put("m4b", "video/mp4");
		mimeTypes.put("m4v", "video/mp4");
		mimeTypes.put("maker", "application/x-maker");
		mimeTypes.put("man", "application/x-troff-man");
		mimeTypes.put("mcif", "chemical/x-mmcif");
		mimeTypes.put("mcm", "chemical/x-macmolecule");
		mimeTypes.put("mdb", "application/msaccess");
		mimeTypes.put("me", "application/x-troff-me");
		mimeTypes.put("mesh", "model/mesh");
		mimeTypes.put("mid", "audio/midi");
		mimeTypes.put("midi", "audio/midi");
		mimeTypes.put("mif", "application/x-mif");
		mimeTypes.put("mm", "application/x-freemind");
		mimeTypes.put("mmd", "chemical/x-macromodel-input");
		mimeTypes.put("mmf", "application/vnd.smaf");
		mimeTypes.put("mml", "text/mathml");
		mimeTypes.put("mmod", "chemical/x-macromodel-input");
		mimeTypes.put("mng", "video/x-mng");
		mimeTypes.put("moc", "text/x-moc");
		mimeTypes.put("mol2", "chemical/x-mol2");
		mimeTypes.put("mol", "chemical/x-mdl-molfile");
		mimeTypes.put("moo", "chemical/x-mopac-out");
		mimeTypes.put("mop", "chemical/x-mopac-input");
		mimeTypes.put("mopcrt", "chemical/x-mopac-input");
		mimeTypes.put("movie", "video/x-sgi-movie");
		mimeTypes.put("mov", "video/quicktime");
		mimeTypes.put("mp2", "audio/mpeg");
		mimeTypes.put("mp3", "audio/mpeg");
		mimeTypes.put("mp4", "video/mp4");
		mimeTypes.put("mpc", "chemical/x-mopac-input");
		mimeTypes.put("mpega", "audio/mpeg");
		mimeTypes.put("mpeg", "video/mpeg");
		mimeTypes.put("mpe", "video/mpeg");
		mimeTypes.put("mpga", "audio/mpeg");
		mimeTypes.put("mpg", "video/mpeg");
		mimeTypes.put("ms", "application/x-troff-ms");
		mimeTypes.put("msh", "model/mesh");
		mimeTypes.put("msi", "application/x-msi");
		mimeTypes.put("mvb", "chemical/x-mopac-vib");
		mimeTypes.put("mxu", "video/vnd.mpegurl");
		mimeTypes.put("nb", "application/mathematica");
		mimeTypes.put("nc", "application/x-netcdf");
		mimeTypes.put("nwc", "application/x-nwc");
		mimeTypes.put("o", "application/x-object");
		mimeTypes.put("oda", "application/oda");
		mimeTypes.put("odb", "application/vnd.oasis.opendocument.database");
		mimeTypes.put("odc", "application/vnd.oasis.opendocument.chart");
		mimeTypes.put("odf", "application/vnd.oasis.opendocument.formula");
		mimeTypes.put("odg", "application/vnd.oasis.opendocument.graphics");
		mimeTypes.put("odi", "application/vnd.oasis.opendocument.image");
		mimeTypes.put("odm", "application/vnd.oasis.opendocument.text-master");
		mimeTypes.put("odp", "application/vnd.oasis.opendocument.presentation");
		mimeTypes.put("ods", "application/vnd.oasis.opendocument.spreadsheet");
		mimeTypes.put("odt", "application/vnd.oasis.opendocument.text");
		mimeTypes.put("oga", "audio/ogg");
		mimeTypes.put("ogg", "application/ogg");
		mimeTypes.put("ogv", "video/ogg");
		mimeTypes.put("ogx", "application/ogg");
		mimeTypes.put("old", "application/x-trash");
		mimeTypes.put("otg", "application/vnd.oasis.opendocument.graphics-template");
		mimeTypes.put("oth", "application/vnd.oasis.opendocument.text-rest");
		mimeTypes.put("otp", "application/vnd.oasis.opendocument.presentation-template");
		mimeTypes.put("ots", "application/vnd.oasis.opendocument.spreadsheet-template");
		mimeTypes.put("ott", "application/vnd.oasis.opendocument.text-template");
		mimeTypes.put("oza", "application/x-oz-application");
		mimeTypes.put("p7r", "application/x-pkcs7-certreqresp");
		mimeTypes.put("pac", "application/x-ns-proxy-autoconfig");
		mimeTypes.put("pas", "text/x-pascal");
		mimeTypes.put("patch", "text/x-diff");
		mimeTypes.put("pat", "image/x-coreldrawpattern");
		mimeTypes.put("pbm", "image/x-portable-bitmap");
		mimeTypes.put("pcap", "application/cap");
		mimeTypes.put("pcf", "application/x-font");
		mimeTypes.put("pcx", "image/pcx");
		mimeTypes.put("pdb", "chemical/x-pdb");
		mimeTypes.put("pdf", "application/pdf");
		mimeTypes.put("pfa", "application/x-font");
		mimeTypes.put("pfb", "application/x-font");
		mimeTypes.put("pgm", "image/x-portable-graymap");
		mimeTypes.put("pgn", "application/x-chess-pgn");
		mimeTypes.put("pgp", "application/pgp-signature");
		mimeTypes.put("php3", "application/x-httpd-php3");
		mimeTypes.put("php3p", "application/x-httpd-php3-preprocessed");
		mimeTypes.put("php4", "application/x-httpd-php4");
		mimeTypes.put("php", "application/x-httpd-php");
		mimeTypes.put("phps", "application/x-httpd-php-source");
		mimeTypes.put("pht", "application/x-httpd-php");
		mimeTypes.put("phtml", "application/x-httpd-php");
		mimeTypes.put("pk", "application/x-tex-pk");
		mimeTypes.put("pls", "audio/x-scpls");
		mimeTypes.put("pl", "text/x-perl");
		mimeTypes.put("pm", "text/x-perl");
		mimeTypes.put("png", "image/png");
		mimeTypes.put("pnm", "image/x-portable-anymap");
		mimeTypes.put("pot", "text/plain");
		mimeTypes.put("ppm", "image/x-portable-pixmap");
		mimeTypes.put("pps", "application/vnd.ms-powerpoint");
		mimeTypes.put("ppt", "application/vnd.ms-powerpoint");
		mimeTypes.put("prf", "application/pics-rules");
		mimeTypes.put("prt", "chemical/x-ncbi-asn1-ascii");
		mimeTypes.put("ps", "application/postscript");
		mimeTypes.put("psd", "image/x-photoshop");
		mimeTypes.put("p", "text/x-pascal");
		mimeTypes.put("pyc", "application/x-python-code");
		mimeTypes.put("pyo", "application/x-python-code");
		mimeTypes.put("py", "text/x-python");
		mimeTypes.put("qtl", "application/x-quicktimeplayer");
		mimeTypes.put("qt", "video/quicktime");
		mimeTypes.put("ra", "audio/x-pn-realaudio");
		mimeTypes.put("ra", "audio/x-realaudio");
		mimeTypes.put("ram", "audio/x-pn-realaudio");
		mimeTypes.put("rar", "application/rar");
		mimeTypes.put("ras", "image/x-cmu-raster");
		mimeTypes.put("rd", "chemical/x-mdl-rdfile");
		mimeTypes.put("rdf", "application/rdf+xml");
		mimeTypes.put("rgb", "image/x-rgb");
		mimeTypes.put("rhtml", "application/x-httpd-eruby");
		mimeTypes.put("rm", "audio/x-pn-realaudio");
		mimeTypes.put("roff", "application/x-troff");
		mimeTypes.put("ros", "chemical/x-rosdal");
		mimeTypes.put("rpm", "application/x-redhat-package-manager");
		mimeTypes.put("rss", "application/rss+xml");
		mimeTypes.put("rtf", "application/rtf");
		mimeTypes.put("rtx", "text/richtext");
		mimeTypes.put("rxn", "chemical/x-mdl-rxnfile");
		mimeTypes.put("sct", "text/scriptlet");
		mimeTypes.put("sd2", "audio/x-sd2");
		mimeTypes.put("sda", "application/vnd.stardivision.draw");
		mimeTypes.put("sdc", "application/vnd.stardivision.calc");
		mimeTypes.put("sd", "chemical/x-mdl-sdfile");
		mimeTypes.put("sdd", "application/vnd.stardivision.impress");
		mimeTypes.put("sdf", "application/vnd.stardivision.math");
		mimeTypes.put("sdf", "chemical/x-mdl-sdfile");
		mimeTypes.put("sds", "application/vnd.stardivision.chart");
		mimeTypes.put("sdw", "application/vnd.stardivision.writer");
		mimeTypes.put("ser", "application/java-serialized-object");
		mimeTypes.put("sgf", "application/x-go-sgf");
		mimeTypes.put("sgl", "application/vnd.stardivision.writer-global");
		mimeTypes.put("sh", "application/x-sh");
		mimeTypes.put("shar", "application/x-shar");
		mimeTypes.put("sh", "text/x-sh");
		mimeTypes.put("shtml", "text/html");
		mimeTypes.put("sid", "audio/prs.sid");
		mimeTypes.put("sik", "application/x-trash");
		mimeTypes.put("silo", "model/mesh");
		mimeTypes.put("sis", "application/vnd.symbian.install");
		mimeTypes.put("sisx", "x-epoc/x-sisx-app");
		mimeTypes.put("sit", "application/x-stuffit");
		mimeTypes.put("sitx", "application/x-stuffit");
		mimeTypes.put("skd", "application/x-koan");
		mimeTypes.put("skm", "application/x-koan");
		mimeTypes.put("skp", "application/x-koan");
		mimeTypes.put("skt", "application/x-koan");
		mimeTypes.put("smi", "application/smil");
		mimeTypes.put("smil", "application/smil");
		mimeTypes.put("snd", "audio/basic");
		mimeTypes.put("spc", "chemical/x-galactic-spc");
		mimeTypes.put("spl", "application/futuresplash");
		mimeTypes.put("spl", "application/x-futuresplash");
		mimeTypes.put("spx", "audio/ogg");
		mimeTypes.put("src", "application/x-wais-source");
		mimeTypes.put("stc", "application/vnd.sun.xml.calc.template");
		mimeTypes.put("std", "application/vnd.sun.xml.draw.template");
		mimeTypes.put("sti", "application/vnd.sun.xml.impress.template");
		mimeTypes.put("stl", "application/vnd.ms-pki.stl");
		mimeTypes.put("stw", "application/vnd.sun.xml.writer.template");
		mimeTypes.put("sty", "text/x-tex");
		mimeTypes.put("sv4cpio", "application/x-sv4cpio");
		mimeTypes.put("sv4crc", "application/x-sv4crc");
		mimeTypes.put("svg", "image/svg+xml");
		mimeTypes.put("svgz", "image/svg+xml");
		mimeTypes.put("sw", "chemical/x-swissprot");
		mimeTypes.put("swf", "application/x-shockwave-flash");
		mimeTypes.put("swfl", "application/x-shockwave-flash");
		mimeTypes.put("sxc", "application/vnd.sun.xml.calc");
		mimeTypes.put("sxd", "application/vnd.sun.xml.draw");
		mimeTypes.put("sxg", "application/vnd.sun.xml.writer.global");
		mimeTypes.put("sxi", "application/vnd.sun.xml.impress");
		mimeTypes.put("sxm", "application/vnd.sun.xml.math");
		mimeTypes.put("sxw", "application/vnd.sun.xml.writer");
		mimeTypes.put("t", "application/x-troff");
		mimeTypes.put("tar", "application/x-tar");
		mimeTypes.put("taz", "application/x-gtar");
		mimeTypes.put("tcl", "application/x-tcl");
		mimeTypes.put("tcl", "text/x-tcl");
		mimeTypes.put("texi", "application/x-texinfo");
		mimeTypes.put("texinfo", "application/x-texinfo");
		mimeTypes.put("tex", "text/x-tex");
		mimeTypes.put("text", "text/plain");
		mimeTypes.put("tgf", "chemical/x-mdl-tgf");
		mimeTypes.put("tgz", "application/x-gtar");
		mimeTypes.put("tiff", "image/tiff");
		mimeTypes.put("tif", "image/tiff");
		mimeTypes.put("tk", "text/x-tcl");
		mimeTypes.put("tm", "text/texmacs");
		mimeTypes.put("torrent", "application/x-bittorrent");
		mimeTypes.put("tr", "application/x-troff");
		mimeTypes.put("tsp", "application/dsptype");
		mimeTypes.put("ts", "text/texmacs");
		mimeTypes.put("tsv", "text/tab-separated-values");
		mimeTypes.put("txt", "text/plain");
		mimeTypes.put("udeb", "application/x-debian-package");
		mimeTypes.put("uls", "text/iuls");
		mimeTypes.put("ustar", "application/x-ustar");
		mimeTypes.put("val", "chemical/x-ncbi-asn1-binary");
		mimeTypes.put("vcd", "application/x-cdlink");
		mimeTypes.put("vcf", "text/x-vcard");
		mimeTypes.put("vcs", "text/x-vcalendar");
		mimeTypes.put("vmd", "chemical/x-vmd");
		mimeTypes.put("vms", "chemical/x-vamas-iso14976");
		mimeTypes.put("vrml", "model/vrml");
		mimeTypes.put("vrml", "x-world/x-vrml");
		mimeTypes.put("vrm", "x-world/x-vrml");
		mimeTypes.put("vsd", "application/vnd.visio");
		mimeTypes.put("wad", "application/x-doom");
		mimeTypes.put("wav", "audio/x-wav");
		mimeTypes.put("wax", "audio/x-ms-wax");
		mimeTypes.put("wbmp", "image/vnd.wap.wbmp");
		mimeTypes.put("wbxml", "application/vnd.wap.wbxml");
		mimeTypes.put("wk", "application/x-123");
		mimeTypes.put("wma", "audio/x-ms-wma");
		mimeTypes.put("wmd", "application/x-ms-wmd");
		mimeTypes.put("wmlc", "application/vnd.wap.wmlc");
		mimeTypes.put("wmlsc", "application/vnd.wap.wmlscriptc");
		mimeTypes.put("wmls", "text/vnd.wap.wmlscript");
		mimeTypes.put("wml", "text/vnd.wap.wml");
		mimeTypes.put("wm", "video/x-ms-wm");
		mimeTypes.put("wmv", "video/x-ms-wmv");
		mimeTypes.put("wmx", "video/x-ms-wmx");
		mimeTypes.put("wmz", "application/x-ms-wmz");
		mimeTypes.put("wp5", "application/wordperfect5.1");
		mimeTypes.put("wpd", "application/wordperfect");
		mimeTypes.put("wrl", "model/vrml");
		mimeTypes.put("wrl", "x-world/x-vrml");
		mimeTypes.put("wsc", "text/scriptlet");
		mimeTypes.put("wvx", "video/x-ms-wvx");
		mimeTypes.put("wz", "application/x-wingz");
		mimeTypes.put("xbm", "image/x-xbitmap");
		mimeTypes.put("xcf", "application/x-xcf");
		mimeTypes.put("xht", "application/xhtml+xml");
		mimeTypes.put("xhtml", "application/xhtml+xml");
		mimeTypes.put("xlb", "application/vnd.ms-excel");
		mimeTypes.put("xls", "application/vnd.ms-excel");
		mimeTypes.put("xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		mimeTypes.put("xlt", "application/vnd.ms-excel");
		mimeTypes.put("xml", "application/xml");
		mimeTypes.put("xpi", "application/x-xpinstall");
		mimeTypes.put("xpm", "image/x-xpixmap");
		mimeTypes.put("xsl", "application/xml");
		mimeTypes.put("xtel", "chemical/x-xtel");
		mimeTypes.put("xul", "application/vnd.mozilla.xul+xml");
		mimeTypes.put("xwd", "image/x-xwindowdump");
		mimeTypes.put("xyz", "chemical/x-xyz");
		mimeTypes.put("zip", "application/zip");
		mimeTypes.put("zmt", "chemical/x-mopac-input");
		mimeTypes.put("docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
		mimeTypes.put("so", "application/octet-stream");
		mimeTypes.put("bz2", "application/x-bzip2");
		mimeTypes.put("gz", "application/x-gzip");
		mimeTypes.put("sgml", "text/sgml");
		mimeTypes.put("sgm", "text/sgml");
		mimeTypes.put("cv", "text/xml");
		mimeTypes.put("flv", "video/x-flv");
		mimeTypes.put("m3u8", "application/x-mpegURL");
		mimeTypes.put("ts", "video/MP2T");

		mimeTypes.entrySet().forEach(e -> mimeTypesInverted.put(e.getValue(), e.getKey()));
		mimeTypesInverted.put(XML, "xml");
		mimeTypesInverted.put(PDF, "pdf");
		mimeTypesInverted.put(TEXT, "txt");
		mimeTypesInverted.put("image/jpeg", "jpg");
		mimeTypesInverted.put("image/png", "png");
	}

}
