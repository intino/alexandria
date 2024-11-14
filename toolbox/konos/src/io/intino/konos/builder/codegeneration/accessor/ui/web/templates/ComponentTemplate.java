package io.intino.konos.builder.codegeneration.accessor.ui.web.templates;

import io.intino.itrules.template.Rule;
import io.intino.itrules.template.Template;

import java.util.ArrayList;
import java.util.List;

import static io.intino.itrules.template.condition.predicates.Predicates.*;
import static io.intino.itrules.template.outputs.Outputs.*;

public class ComponentTemplate extends Template {

	public List<Rule> ruleSet() {
		List<Rule> rules = new ArrayList<>();
		rules.add(rule().condition(allTypes("reference")).output(literal("<Displays")).output(placeholder("name", "firstUppercase")).output(literal(" context={this._context.bind(this)} owner={this._owner.bind(this)} id=\"")).output(expression().output(placeholder("parentPath")).output(literal("."))).output(placeholder("id")).output(literal("\"")).output(placeholder("properties", "common")).output(placeholder("properties", "specific")).output(literal("></Displays")).output(placeholder("name", "firstUppercase")).output(literal(">")));
		rules.add(rule().condition(all(allTypes("child"), trigger("declaration"))));
		rules.add(rule().condition(all(allTypes("block", "child"), trigger("child"))).output(placeholder("value")));
		rules.add(rule().condition(all(allTypes("child"), trigger("child"))).output(placeholder("value")));
		rules.add(rule().condition(all(allTypes("child"), trigger("add"))));
		rules.add(rule().condition(allTypes("component", "child", "item")));
		rules.add(rule().condition(allTypes("component", "child")).output(literal("<")).output(placeholder("extends")).output(literal(" context={this._context.bind(this)} owner={this._owner.bind(this)} id=\"")).output(expression().output(placeholder("parentPath")).output(literal("."))).output(placeholder("id")).output(literal("\"")).output(placeholder("properties", "common")).output(placeholder("properties", "specific")).output(expression().output(literal(" ")).output(placeholder("code"))).output(literal(">\n\t")).output(expression().output(placeholder("reference").multiple("\n"))).output(literal("\n\t")).output(expression().output(placeholder("methods"))).output(literal("\n\t")).output(expression().output(placeholder("component", "child").multiple("\n"))).output(literal("\n")).output(expression().output(literal("</")).output(placeholder("extends")).output(literal(">"))));
		rules.add(rule().condition(allTypes("extends", "multiple", "image")).output(literal("UiMultipleImage")));
		rules.add(rule().condition(allTypes("extends", "multiple")).output(literal("UiMultiple")));
		rules.add(rule().condition(allTypes("extends", "displaystamp")).output(literal("Ui")).output(placeholder("type", "firstUpperCase")).output(placeholder("facet").multiple("")));
		rules.add(rule().condition(allTypes("extends", "proxystamp")).output(literal("UiProxyStamp")));
		rules.add(rule().condition(allTypes("extends", "basestamp")).output(literal("Displays")).output(placeholder("type", "firstUpperCase")));
		rules.add(rule().condition(allTypes("extends")).output(literal("Ui")).output(placeholder("type", "firstUpperCase")).output(placeholder("facet").multiple("")));
		rules.add(rule().condition(allTypes("facet")).output(placeholder("name", "firstUpperCase")));
		rules.add(rule().condition(allTypes("method")));
		rules.add(rule().condition(all(allTypes("properties", "actionable"), trigger("common"))).output(expression().output(literal(" title=\"")).output(placeholder("title")).output(literal("\""))).output(expression().output(literal(" name=\"")).output(placeholder("name")).output(literal("\""))).output(expression().output(literal(" target=\"")).output(placeholder("target")).output(literal("\""))).output(expression().output(literal(" mode=\"")).output(placeholder("actionableMode")).output(literal("\""))).output(expression().output(literal(" icon=\"")).output(placeholder("icon")).output(literal("\""))).output(expression().output(literal(" titlePosition=\"")).output(placeholder("titlePosition")).output(literal("\""))).output(expression().output(literal(" affirmed=\"")).output(placeholder("affirmed")).output(literal("\""))).output(expression().output(literal(" traceable={")).output(placeholder("traceable")).output(literal("}"))).output(expression().output(literal(" signed={{mode:\"")).output(placeholder("signMode")).output(literal("\",text:\"")).output(placeholder("signText")).output(literal("\",reason:\"")).output(placeholder("reasonText")).output(literal("\"}}"))).output(expression().output(literal(" readonly={")).output(placeholder("readonly")).output(literal("}"))).output(expression().output(literal(" size=\"")).output(placeholder("size")).output(literal("\""))).output(expression().output(literal(" format=\"")).output(placeholder("format").multiple(" ")).output(literal("\""))).output(expression().output(literal(" highlighted=\"")).output(placeholder("highlighted")).output(literal("\""))).output(expression().output(literal(" visible={")).output(placeholder("visible")).output(literal("}"))).output(expression().output(literal(" style=\"")).output(placeholder("style")).output(literal("\""))).output(expression().output(literal(" color=\"")).output(placeholder("color")).output(literal("\""))).output(expression().output(literal(" shortcut={")).output(placeholder("shortcut")).output(literal("}"))));
		rules.add(rule().condition(all(allTypes("properties", "abstractslider"), trigger("common"))).output(expression().output(literal(" label=\"")).output(placeholder("label")).output(literal("\""))).output(expression().output(literal(" format=\"")).output(placeholder("format").multiple(" ")).output(literal("\""))).output(expression().output(literal(" color=\"")).output(placeholder("color")).output(literal("\""))).output(expression().output(literal(" traceable={")).output(placeholder("traceable")).output(literal("}"))).output(expression().output(literal(" arrangement=\"")).output(placeholder("arrangement")).output(literal("\""))).output(expression().output(literal(" animation={{interval:")).output(placeholder("interval")).output(literal(",loop:")).output(placeholder("loop")).output(literal("}}"))).output(expression().output(literal(" readonly={")).output(placeholder("readonly")).output(literal("}"))).output(expression().output(literal(" visible={")).output(placeholder("visible")).output(literal("}"))).output(expression().output(literal(" style=\"")).output(placeholder("style")).output(literal("\""))).output(expression().output(literal(" position=\"")).output(placeholder("position")).output(literal("\""))));
		rules.add(rule().condition(all(allTypes("properties", "appDirectory"), trigger("common"))).output(expression().output(literal(" icon=\"")).output(placeholder("icon")).output(literal("\""))).output(expression().output(literal(" visible={")).output(placeholder("visible")).output(literal("}"))).output(expression().output(literal(" style=\"")).output(placeholder("style")).output(literal("\""))));
		rules.add(rule().condition(all(allTypes("properties", "dashboard"), trigger("common"))).output(expression().output(literal(" width=\"")).output(placeholder("width")).output(literal("\""))).output(expression().output(literal(" height=\"")).output(placeholder("height")).output(literal("\""))).output(expression().output(literal(" style=\"")).output(placeholder("style")).output(literal("\""))));
		rules.add(rule().condition(all(allTypes("properties"), trigger("common"))).output(expression().output(literal(" name=\"")).output(placeholder("name")).output(literal("\""))).output(expression().output(literal(" label=\"")).output(placeholder("label")).output(literal("\""))).output(expression().output(literal(" format=\"")).output(placeholder("format").multiple(" ")).output(literal("\""))).output(expression().output(literal(" color=\"")).output(placeholder("color")).output(literal("\""))).output(expression().output(literal(" visible={")).output(placeholder("visible")).output(literal("}"))).output(expression().output(literal(" traceable={")).output(placeholder("traceable")).output(literal("}"))).output(expression().output(literal(" multiple={{instances:\"")).output(placeholder("instances")).output(literal("\",arrangement:\"")).output(placeholder("multipleArrangement")).output(literal("\",noItemsMessage:\"")).output(placeholder("multipleNoItemsMessage")).output(literal("\",spacing:")).output(placeholder("multipleSpacing")).output(literal(",editable:")).output(placeholder("multipleEditable")).output(literal(",wrap:")).output(placeholder("multipleWrapItems")).output(literal(",collapsed:")).output(placeholder("multipleCollapsed")).output(literal(",count:{min:")).output(placeholder("multipleMin")).output(literal(",max:")).output(placeholder("multipleMax")).output(literal("}}}"))).output(expression().output(literal(" style=\"")).output(placeholder("style")).output(literal("\""))));
		rules.add(rule().condition(all(allTypes("properties", "openpopover"), trigger("specific"))).output(expression().output(literal(" triggerEvent=\"")).output(placeholder("triggerEvent")).output(literal("\""))));
		rules.add(rule().condition(all(allTypes("properties", "signtext"), trigger("specific"))).output(expression().output(literal(" text=\"")).output(placeholder("text")).output(literal("\""))).output(expression().output(literal(" signFormat=\"")).output(placeholder("signFormat")).output(literal("\""))));
		rules.add(rule().condition(all(allTypes("properties", "kpi"), trigger("specific"))).output(expression().output(literal(" mode=\"")).output(placeholder("mode")).output(literal("\""))).output(expression().output(literal(" unit=\"")).output(placeholder("unit")).output(literal("\""))).output(expression().output(literal(" textColor=\"")).output(placeholder("textColor")).output(literal("\""))).output(expression().output(literal(" backgroundColor=\"")).output(placeholder("backgroundColor")).output(literal("\""))).output(expression().output(literal(" highlightedColor=\"")).output(placeholder("highlightedColor")).output(literal("\""))).output(expression().output(literal(" value=\"")).output(placeholder("value")).output(literal("\""))).output(expression().output(literal(" size=\"")).output(placeholder("size")).output(literal("\""))).output(expression().output(literal(" textPosition=\"")).output(placeholder("textPosition")).output(literal("\""))));
		rules.add(rule().condition(all(allTypes("properties", "template"), trigger("specific"))).output(expression().output(literal(" layout=\"")).output(placeholder("layout").multiple(" ")).output(literal("\""))).output(expression().output(literal(" width=\"")).output(placeholder("width")).output(literal("\""))).output(expression().output(literal(" height=\"")).output(placeholder("height")).output(literal("\""))).output(expression().output(literal(" spacing=\"")).output(placeholder("spacing")).output(literal("\""))));
		rules.add(rule().condition(all(allTypes("properties", "basestamp"), trigger("specific"))).output(expression().output(literal(" spacing=\"")).output(placeholder("spacing")).output(literal("\""))));
		rules.add(rule().condition(all(allTypes("properties", "materialicon"), trigger("specific"))).output(expression().output(literal(" title=\"")).output(placeholder("title")).output(literal("\""))).output(expression().output(literal(" icon=\"")).output(placeholder("icon")).output(literal("\""))).output(expression().output(literal(" titlePosition=\"")).output(placeholder("titlePosition")).output(literal("\""))));
		rules.add(rule().condition(all(allTypes("properties", "selector"), trigger("specific"))).output(expression().output(literal(" multipleSelection={")).output(placeholder("multipleSelection")).output(literal("}"))).output(expression().output(literal(" readonly={")).output(placeholder("readonly")).output(literal("}"))).output(expression().output(literal(" focused={")).output(placeholder("focused")).output(literal("}"))).output(expression().output(literal(" placeholder=\"")).output(placeholder("placeholder")).output(literal("\""))).output(expression().output(literal(" selected=\"")).output(placeholder("selected")).output(literal("\""))).output(expression().output(literal(" layout=\"")).output(placeholder("layout")).output(literal("\""))).output(expression().output(literal(" size=\"")).output(placeholder("size")).output(literal("\""))).output(expression().output(literal(" maxMenuHeight={")).output(placeholder("maxMenuHeight")).output(literal("}"))).output(expression().output(literal(" allowOther=\"")).output(placeholder("allowOther")).output(literal("\""))).output(expression().output(literal(" scrollButtons=\"")).output(placeholder("scrollButtons")).output(literal("\""))).output(expression().output(literal(" view=\"")).output(placeholder("view")).output(literal("\""))));
		rules.add(rule().condition(all(allTypes("properties", "image"), trigger("specific"))).output(expression().output(literal(" width=\"")).output(placeholder("width")).output(literal("\""))).output(expression().output(literal(" height=\"")).output(placeholder("height")).output(literal("\""))).output(expression().output(literal(" mobileReduceFactor={")).output(placeholder("mobileReduceFactor")).output(literal("}"))).output(expression().output(literal(" allowFullscreen={")).output(placeholder("allowFullscreen")).output(literal("}"))).output(expression().output(literal(" colorInvertedWithDarkMode={")).output(placeholder("colorInvertedWithDarkMode")).output(literal("}"))));
		rules.add(rule().condition(all(allTypes("properties", "file"), trigger("specific"))).output(expression().output(literal(" width=\"")).output(placeholder("width")).output(literal("\""))).output(expression().output(literal(" height=\"")).output(placeholder("height")).output(literal("\""))).output(expression().output(literal(" preview={")).output(placeholder("preview")).output(literal("}"))).output(expression().output(literal(" dropZone={")).output(placeholder("dropZone")).output(literal("}"))).output(expression().output(literal(" pasteZone={")).output(placeholder("pasteZone")).output(literal("}"))).output(expression().output(literal(" maxSize={")).output(placeholder("maxSize")).output(literal("}"))).output(expression().output(literal(" allowedTypes={[")).output(placeholder("allowedTypes")).output(literal("]}"))));
		rules.add(rule().condition(all(allTypes("properties", "chart"), trigger("specific"))).output(expression().output(literal(" width=\"")).output(placeholder("width")).output(literal("\""))).output(expression().output(literal(" height=\"")).output(placeholder("height")).output(literal("\""))));
		rules.add(rule().condition(all(allTypes("properties", "alertdialog"), trigger("specific"))).output(expression().output(literal(" title=\"")).output(placeholder("title")).output(literal("\""))).output(expression().output(literal(" modal={")).output(placeholder("modal")).output(literal("}"))).output(expression().output(literal(" fullscreen={")).output(placeholder("fullscreen")).output(literal("}"))).output(expression().output(literal(" message=\"")).output(placeholder("message")).output(literal("\""))).output(expression().output(literal(" closeLabel=\"")).output(placeholder("closeLabel")).output(literal("\""))).output(expression().output(literal(" acceptLabel=\"")).output(placeholder("acceptLabel")).output(literal("\""))).output(expression().output(literal(" width=\"")).output(placeholder("width")).output(literal("\""))).output(expression().output(literal(" height=\"")).output(placeholder("height")).output(literal("\""))).output(expression().output(literal(" animation={{mode:\"")).output(placeholder("mode")).output(literal("\",direction:\"")).output(placeholder("transitionDirection")).output(literal("\",duration:\"")).output(placeholder("transitionDuration")).output(literal("\"}}"))));
		rules.add(rule().condition(all(allTypes("properties", "abstractdialog"), trigger("specific"))).output(expression().output(literal(" title=\"")).output(placeholder("title")).output(literal("\""))).output(expression().output(literal(" modal={")).output(placeholder("modal")).output(literal("}"))).output(expression().output(literal(" fullscreen={")).output(placeholder("fullscreen")).output(literal("}"))).output(expression().output(literal(" width=\"")).output(placeholder("width")).output(literal("\""))).output(expression().output(literal(" height=\"")).output(placeholder("height")).output(literal("\""))).output(expression().output(literal(" animation={{mode:\"")).output(placeholder("mode")).output(literal("\",direction:\"")).output(placeholder("transitionDirection")).output(literal("\",duration:\"")).output(placeholder("transitionDuration")).output(literal("\"}}"))));
		rules.add(rule().condition(all(allTypes("properties", "block"), trigger("specific"))).output(expression().output(literal(" layout=\"")).output(placeholder("layout").multiple(" ")).output(literal("\""))).output(expression().output(literal(" width=\"")).output(placeholder("width")).output(literal("\""))).output(expression().output(literal(" height=\"")).output(placeholder("height")).output(literal("\""))).output(expression().output(literal(" spacing=\"")).output(placeholder("spacing")).output(literal("\""))).output(expression().output(literal(" ")).output(placeholder("paper"))).output(expression().output(literal(" ")).output(placeholder("badge"))).output(expression().output(literal(" ")).output(placeholder("drawer"))).output(expression().output(literal(" ")).output(placeholder("popover"))).output(expression().output(literal(" animation={{mode:\"")).output(placeholder("mode")).output(literal("\",direction:\"")).output(placeholder("transitionDirection")).output(literal("\",duration:\"")).output(placeholder("transitionDuration")).output(literal("\"}}"))).output(expression().output(literal(" hidden=\"")).output(placeholder("hidden")).output(literal("\""))).output(expression().output(literal(" splitMobileLabel=\"")).output(placeholder("splitMobileLabel")).output(literal("\""))).output(expression().output(literal(" autoSize={")).output(placeholder("autoSize")).output(literal("}"))));
		rules.add(rule().condition(all(allTypes("properties", "date"), trigger("specific"))).output(expression().output(literal(" pattern=\"")).output(placeholder("pattern")).output(literal("\""))).output(expression().output(literal(" mode=\"")).output(placeholder("mode")).output(literal("\""))).output(expression().output(literal(" value={new Date(")).output(placeholder("value")).output(literal(")}"))).output(expression().output(literal(" timePicker={")).output(placeholder("timePicker")).output(literal("}"))).output(expression().output(literal(" mask=\"")).output(placeholder("mask")).output(literal("\""))).output(expression().output(literal(" embedded={")).output(placeholder("embedded")).output(literal("}"))).output(expression().output(literal(" allowEmpty={")).output(placeholder("allowEmpty")).output(literal("}"))).output(expression().output(literal(" views={[\"")).output(placeholder("view").multiple("\",\"")).output(literal("\"]}"))).output(expression().output(literal(" shrink={")).output(placeholder("shrink")).output(literal("}"))));
		rules.add(rule().condition(all(allTypes("properties", "user"), trigger("specific"))).output(expression().output(literal(" mode=\"")).output(placeholder("mode")).output(literal("\""))));
		rules.add(rule().condition(all(allTypes("properties", "number"), trigger("specific"))).output(expression().output(literal(" value={")).output(placeholder("value")).output(literal("}"))).output(expression().output(literal(" prefix=\"")).output(placeholder("prefix")).output(literal("\""))).output(expression().output(literal(" suffix=\"")).output(placeholder("suffix")).output(literal("\""))).output(expression().output(literal(" min={")).output(placeholder("min")).output(literal("}"))).output(expression().output(literal(" max={")).output(placeholder("max")).output(literal("}"))).output(expression().output(literal(" step={")).output(placeholder("step")).output(literal("}"))).output(expression().output(literal(" readonly={")).output(placeholder("readonly")).output(literal("}"))).output(expression().output(literal(" focused={")).output(placeholder("focused")).output(literal("}"))).output(expression().output(literal(" decimals={")).output(placeholder("decimals")).output(literal("}"))).output(expression().output(literal(" expanded={")).output(placeholder("expanded")).output(literal("}"))).output(expression().output(literal(" helperText=\"")).output(placeholder("helperText")).output(literal("\""))).output(expression().output(literal(" shrink={")).output(placeholder("shrink")).output(literal("}"))));
		rules.add(rule().condition(all(allTypes("properties", "header"), trigger("specific"))).output(expression().output(literal(" position=\"")).output(placeholder("position")).output(literal("\""))).output(expression().output(literal(" width=\"")).output(placeholder("width")).output(literal("\""))).output(expression().output(literal(" height=\"")).output(placeholder("height")).output(literal("\""))).output(expression().output(literal(" elevation={")).output(placeholder("elevation")).output(literal("}"))));
		rules.add(rule().condition(all(allTypes("properties", "code"), trigger("specific"))).output(expression().output(literal(" mode=\"")).output(placeholder("mode")).output(literal("\""))).output(expression().output(literal(" language=\"")).output(placeholder("language")).output(literal("\""))).output(expression().output(literal(" highlighted=")).output(placeholder("highlighted"))));
		rules.add(rule().condition(all(allTypes("properties", "text"), trigger("specific"))).output(expression().output(literal(" mode=\"")).output(placeholder("mode")).output(literal("\""))).output(expression().output(literal(" editionMode=\"")).output(placeholder("editionMode")).output(literal("\""))).output(expression().output(literal(" maxLength={")).output(placeholder("maxLength")).output(literal("}"))).output(expression().output(literal(" rows={")).output(placeholder("rows")).output(literal("}"))).output(expression().output(literal(" prefix=\"")).output(placeholder("prefix")).output(literal("\""))).output(expression().output(literal(" suffix=\"")).output(placeholder("suffix")).output(literal("\""))).output(expression().output(literal(" translate={")).output(placeholder("translate")).output(literal("}"))).output(expression().output(literal(" cropWithEllipsis={")).output(placeholder("cropWithEllipsis")).output(literal("}"))).output(expression().output(literal(" value=\"")).output(placeholder("defaultValue")).output(literal("\""))).output(expression().output(literal(" placeholder=\"")).output(placeholder("placeholder")).output(literal("\""))).output(expression().output(literal(" readonly={")).output(placeholder("readonly")).output(literal("}"))).output(expression().output(literal(" focused={")).output(placeholder("focused")).output(literal("}"))).output(expression().output(literal(" highlighted={")).output(placeholder("highlighted")).output(literal("}"))).output(expression().output(literal(" type=\"")).output(placeholder("type")).output(literal("\""))).output(expression().output(literal(" helperText=\"")).output(placeholder("helperText")).output(literal("\""))).output(expression().output(literal(" shrink={")).output(placeholder("shrink")).output(literal("}"))));
		rules.add(rule().condition(all(allTypes("properties", "location"), trigger("specific"))).output(expression().output(literal(" center={{lat:")).output(placeholder("centerLat")).output(literal(",lng:")).output(placeholder("centerLng")).output(literal("}}"))).output(expression().output(literal(" zoom={{min:")).output(placeholder("zoomMin")).output(literal(",max:")).output(placeholder("zoomMax")).output(literal(",defaultZoom:")).output(placeholder("zoomDefault")).output(literal("}}"))).output(expression().output(literal(" modes={[\"")).output(placeholder("mode").multiple("\",\"")).output(literal("\"]}"))).output(expression().output(literal(" controls=\"")).output(placeholder("controls")).output(literal("\""))));
		rules.add(rule().condition(all(allTypes("properties", "map"), trigger("specific"))).output(expression().output(literal(" pageSize={")).output(placeholder("pageSize")).output(literal("}"))).output(expression().output(literal(" type=\"")).output(placeholder("type")).output(literal("\""))).output(expression().output(literal(" itemHeight={")).output(placeholder("itemHeight")).output(literal("}"))).output(expression().output(literal(" center={{lat:")).output(placeholder("centerLat")).output(literal(",lng:")).output(placeholder("centerLng")).output(literal("}}"))).output(expression().output(literal(" zoom={{min:")).output(placeholder("zoomMin")).output(literal(",max:")).output(placeholder("zoomMax")).output(literal(",defaultZoom:")).output(placeholder("zoomDefault")).output(literal("}}"))).output(expression().output(literal(" controls=\"")).output(placeholder("controls")).output(literal("\""))).output(expression().output(literal(" selection=\"")).output(placeholder("selection")).output(literal("\""))));
		rules.add(rule().condition(all(allTypes("properties", "collection"), trigger("specific"))).output(expression().output(literal(" noItemsMessage=\"")).output(placeholder("noItemsMessage")).output(literal("\""))).output(expression().output(literal(" noItemsFoundMessage=\"")).output(placeholder("noItemsFoundMessage")).output(literal("\""))).output(expression().output(literal(" pageSize={")).output(placeholder("pageSize")).output(literal("}"))).output(expression().output(literal(" itemHeight={")).output(placeholder("itemHeight")).output(literal("}"))).output(expression().output(literal(" scrollingMark={")).output(placeholder("scrollingMark")).output(literal("}"))).output(expression().output(literal(" navigable=\"")).output(placeholder("navigable")).output(literal("\""))).output(expression().output(literal(" selection=\"")).output(placeholder("selection")).output(literal("\""))));
		rules.add(rule().condition(all(allTypes("properties", "heading"), trigger("specific"))).output(literal(" style={{width:\"")).output(placeholder("width")).output(literal("%\",paddingRight:\"10px\"")).output(expression().output(literal(",")).output(placeholder("paddingLeft")).output(literal(":\"10px\""))).output(literal("}}")).output(expression().output(literal(" hidden=\"")).output(placeholder("hidden")).output(literal("\""))));
		rules.add(rule().condition(all(allTypes("properties", "item"), trigger("specific"))).output(literal(" style={{width:\"")).output(placeholder("width")).output(literal("%\",paddingRight:\"10px\"")).output(expression().output(literal(",")).output(placeholder("paddingLeft")).output(literal(":\"10px\""))).output(literal("}}")).output(expression().output(literal(" hidden=\"")).output(placeholder("hidden")).output(literal("\""))));
		rules.add(rule().condition(all(allTypes("properties", "spinner"), trigger("specific"))).output(expression().output(literal(" mode=\"")).output(placeholder("mode")).output(literal("\""))).output(expression().output(literal(" size={")).output(placeholder("size")).output(literal("}"))));
		rules.add(rule().condition(all(allTypes("properties", "switch"), trigger("specific"))).output(expression().output(literal(" state=\"")).output(placeholder("state")).output(literal("\""))));
		rules.add(rule().condition(all(allTypes("properties", "toggle"), trigger("specific"))).output(expression().output(literal(" state=\"")).output(placeholder("state")).output(literal("\""))));
		rules.add(rule().condition(all(allTypes("properties", "splitbutton"), trigger("specific"))).output(expression().output(literal(" options={[\"")).output(placeholder("option").multiple("\",\"")).output(literal("\"]}"))).output(expression().output(literal(" defaultOption=\"")).output(placeholder("default")).output(literal("\""))).output(expression().output(literal(" icon=\"")).output(placeholder("icon")).output(literal("\""))).output(expression().output(literal(" titlePosition=\"")).output(placeholder("titlePosition")).output(literal("\""))));
		rules.add(rule().condition(all(allTypes("properties", "export"), trigger("specific"))).output(expression().output(literal(" from={")).output(placeholder("from")).output(literal("}"))).output(expression().output(literal(" to={")).output(placeholder("to")).output(literal("}"))).output(expression().output(literal(" min={")).output(placeholder("min")).output(literal("}"))).output(expression().output(literal(" max={")).output(placeholder("max")).output(literal("}"))).output(expression().output(literal(" range={{min:")).output(placeholder("rangeMin")).output(literal(",max:")).output(placeholder("rangeMax")).output(literal("}}"))).output(expression().output(literal(" options={[\"")).output(placeholder("option").multiple("\",\"")).output(literal("\"]}"))));
		rules.add(rule().condition(all(allTypes("properties", "download"), trigger("specific"))).output(expression().output(literal(" options={[\"")).output(placeholder("option").multiple("\",\"")).output(literal("\"]}"))));
		rules.add(rule().condition(all(allTypes("properties", "download", "selection"), trigger("specific"))).output(expression().output(literal(" options={[\"")).output(placeholder("option").multiple("\",\"")).output(literal("\"]}"))));
		rules.add(rule().condition(all(allTypes("properties", "searchbox"), trigger("specific"))).output(expression().output(literal(" placeholder=\"")).output(placeholder("placeholder")).output(literal("\""))).output(expression().output(literal(" showCountMessage=\"")).output(placeholder("showCountMessage")).output(literal("\""))));
		rules.add(rule().condition(all(allTypes("properties", "slider"), trigger("specific"))).output(expression().output(literal(" range={{min:")).output(placeholder("min")).output(literal(",max:")).output(placeholder("max")).output(literal("}}"))).output(expression().output(literal(" value={")).output(placeholder("value")).output(literal("}"))));
		rules.add(rule().condition(all(allTypes("properties", "rangeslider"), trigger("specific"))).output(expression().output(literal(" range={{min:")).output(placeholder("min")).output(literal(",max:")).output(placeholder("max")).output(literal("}}"))).output(expression().output(literal(" value={[")).output(placeholder("from")).output(literal(",")).output(placeholder("to")).output(literal("]}"))).output(expression().output(literal(" minimumDistance={")).output(placeholder("minimumDistance")).output(literal("}"))));
		rules.add(rule().condition(all(allTypes("properties", "grouping"), trigger("specific"))).output(expression().output(literal(" pageSize={")).output(placeholder("pageSize")).output(literal("}"))).output(expression().output(literal(" placeholder=\"")).output(placeholder("placeholder")).output(literal("\""))));
		rules.add(rule().condition(all(allTypes("properties", "stepper"), trigger("specific"))).output(literal("   ")).output(expression().output(literal(" orientation=\"")).output(placeholder("orientation", "lowercase")).output(literal("\""))).output(expression().output(literal(" position=\"")).output(placeholder("position", "lowercase")).output(literal("\""))));
		rules.add(rule().condition(all(allTypes("properties", "frame"), trigger("specific"))).output(literal("   ")).output(expression().output(literal(" width=\"")).output(placeholder("width")).output(literal("\""))).output(expression().output(literal(" height=\"")).output(placeholder("height")).output(literal("\""))).output(expression().output(literal(" url=\"")).output(placeholder("url")).output(literal("\""))));
		rules.add(rule().condition(all(allTypes("properties", "sorting"), trigger("specific"))).output(expression().output(literal(" mode=\"")).output(placeholder("mode")).output(literal("\""))).output(expression().output(literal(" align=\"")).output(placeholder("align")).output(literal("\""))));
		rules.add(rule().condition(all(allTypes("properties", "htmlviewer"), trigger("specific"))).output(expression().output(literal(" content=\"")).output(placeholder("content")).output(literal("\""))));
		rules.add(rule().condition(all(allTypes("properties", "timeline"), trigger("specific"))).output(expression().output(literal(" mode=\"")).output(placeholder("mode")).output(literal("\""))));
		rules.add(rule().condition(all(allTypes("properties", "documenteditor", "collabora"), trigger("specific"))).output(expression().output(literal(" editorUrl=\"")).output(placeholder("editorUrl")).output(literal("\""))).output(expression().output(literal(" accessToken=\"")).output(placeholder("accessToken")).output(literal("\""))));
		rules.add(rule().condition(all(allTypes("properties", "eventline"), trigger("specific"))).output(expression().output(literal(" arrangement=\"")).output(placeholder("arrangement")).output(literal("\""))).output(literal("\n")).output(expression().output(literal(" toolbarArrangement=\"")).output(placeholder("toolbarArrangement")).output(literal("\""))));
		rules.add(rule().condition(all(allTypes("properties", "microsite"), trigger("specific"))).output(expression().output(literal(" downloadOperations={[\"")).output(placeholder("downloadOperation").multiple("\",\"")).output(literal("\"]}"))));
		rules.add(rule().condition(all(allTypes("properties"), trigger("specific"))));
		rules.add(rule().condition(allTypes("badge")).output(literal("mode=\"")).output(placeholder("mode")).output(literal("\"")).output(expression().output(literal(" value={")).output(placeholder("value")).output(literal("}"))).output(expression().output(literal(" max={")).output(placeholder("max")).output(literal("}"))).output(expression().output(literal(" showZero={")).output(placeholder("showZero")).output(literal("}"))));
		rules.add(rule().condition(allTypes("drawer")).output(literal("position=\"")).output(placeholder("position")).output(literal("\" variant=\"")).output(placeholder("variant")).output(literal("\"")));
		rules.add(rule().condition(allTypes("popover")).output(literal("position=\"")).output(placeholder("position")).output(literal("\"")));
		rules.add(rule().condition(allTypes("code")).output(literal("value=\"")).output(placeholder("value")).output(literal("\"")));
		rules.add(rule().condition(allTypes("highlighted")).output(literal("{text:\"")).output(placeholder("text")).output(literal("\",background:\"")).output(placeholder("background")).output(literal("\"}")));
		rules.add(rule().condition(allTypes("actionableMode")).output(placeholder("mode")));
		rules.add(rule().condition(allTypes("histogram")).output(literal("{alwaysVisible:")).output(placeholder("alwaysVisible")).output(literal(",type:\"")).output(placeholder("type")).output(literal("\"}")));
		rules.add(rule().condition(allTypes("shortcut")).output(literal("{key:\"")).output(placeholder("key")).output(literal("\",altKey:")).output(placeholder("altKey")).output(literal(",ctrlKey:")).output(placeholder("ctrlKey")).output(literal(",shiftKey:")).output(placeholder("shiftKey")).output(literal(",visible:")).output(placeholder("visible")).output(literal("}")));
		return rules;
	}

	public String render(Object object) {
		return new io.intino.itrules.Engine(this).render(object);
	}

	public String render(Object object, java.util.Map<String, io.intino.itrules.Formatter> formatters) {
		return new io.intino.itrules.Engine(this).addAll(formatters).render(object);
	}
}