package io.intino.konos.builder.codegeneration.ui.passiveview;

import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.ui.ElementRenderer;
import io.intino.konos.builder.codegeneration.ui.TemplateProvider;
import io.intino.konos.model.graph.Component;
import io.intino.konos.model.graph.Display;
import io.intino.konos.model.graph.PassiveView;
import io.intino.konos.model.graph.PassiveView.Notification;
import io.intino.konos.model.graph.PassiveView.Request;
import io.intino.konos.model.graph.decorated.DecoratedDisplay;
import io.intino.tara.magritte.Layer;
import org.siani.itrules.Template;
import org.siani.itrules.model.Frame;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;
import static io.intino.konos.model.graph.PassiveView.Request.ResponseType.Asset;

public abstract class PassiveViewRenderer<C extends PassiveView> extends ElementRenderer<C> {

	protected PassiveViewRenderer(Settings settings, C element, TemplateProvider templateProvider, Target target) {
		super(settings, element, templateProvider, target);
	}

	@Override
	public Frame buildFrame() {
		Frame frame = super.buildFrame();
		String type = type();
		frame.addSlot("id", shortId(element));
		frame.addSlot("type", type);
		addAccessorType(frame);
		if (!type.equalsIgnoreCase("display")) frame.addSlot("packageType", type.toLowerCase());
		frame.addSlot("packageTypeRelativeDirectory", typeOf(element).equalsIgnoreCase("display") ? "" : "../");
		frame.addSlot("parentType", parentType());
		frame.addSlot("name", nameOf(element));
		frame.addSlot("notification", framesOfNotifications(element.notificationList()));
		frame.addSlot("request", framesOfRequests(element.requestList()));
		return frame;
	}

	protected void createPassiveViewFiles(Frame frame) {
		writeNotifier(frame);
		writeRequester(frame);
		writePushRequester(frame);
	}

	protected String type() {
		return typeOf(element.a$(Display.class));
	}

	private void writeRequester(Frame frame) {
		writeRequester(element, frame);
	}

	protected void writeRequester(PassiveView element, Frame frame) {
		writeFrame(new File(gen(), format(Requesters)), snakeCaseToCamelCase(element.name$() + (isAccessible(frame) ? "Proxy" : "") + "Requester"), displayRequesterTemplate().format(frame));
	}

	private void writePushRequester(Frame frame) {
		writePushRequester(element, frame);
	}

	protected void writePushRequester(PassiveView element, Frame frame) {
		Template template = displayPushRequesterTemplate();
		boolean accessible = isAccessible(frame);
		if (accessible || template == null) return;
		writeFrame(new File(gen(), format(Requesters)), snakeCaseToCamelCase(element.name$() + "PushRequester"), template.format(frame));
	}

	private boolean isAccessible(Frame frame) {
		return Arrays.asList(frame.types()).contains("accessible");
	}

	private void writeNotifier(Frame frame) {
		writeNotifier(element, frame);
	}

	protected void writeNotifier(PassiveView element, Frame frame) {
		writeFrame(new File(gen(), format(Notifiers)), snakeCaseToCamelCase(element.name$() + (isAccessible(frame) ? "Proxy" : "") + "Notifier"), displayNotifierTemplate().format(frame));
	}

	private Template displayNotifierTemplate() {
		return setup(notifierTemplate());
	}

	private Template displayRequesterTemplate() {
		return setup(requesterTemplate());
	}

	private Template displayPushRequesterTemplate() {
		Template template = pushRequesterTemplate();
		return template != null ? setup(template) : null;
	}

	private Frame parentType() {
		return new Frame().addSlot(type(),"").addSlot("value", type());
	}

	private Frame[] framesOfNotifications(List<Notification> notifications) {
		return notifications.stream().map(this::frameOf).toArray(Frame[]::new);
	}

	private Frame frameOf(Notification notification) {
		final Frame frame = new Frame().addTypes("notification");
		frame.addSlot("name", notification.name$());
		frame.addSlot("target", notification.to().name());
		if (notification.isType()) {
			final Frame parameterFrame = new Frame().addTypes("parameter", notification.asType().type(), notification.asType().getClass().getSimpleName().replace("Data", "")).addSlot("value", notification.asType().type());
			if (notification.isList()) parameterFrame.addTypes("list");
			frame.addSlot("parameter", parameterFrame);
		}
		return frame;
	}

	private Frame[] framesOfRequests(List<Request> requests) {
		return requests.stream().map(r -> frameOf(element, r, packageName())).toArray(Frame[]::new);
	}

	public static Frame frameOf(Layer element, Request request, String packageName) {
		final Frame frame = new Frame().addTypes("request");
		frame.addSlot("display", element.name$());
		if (request.responseType().equals(Asset)) frame.addTypes("asset");
		if (request.isFile()) frame.addTypes("file");
		frame.addSlot("name", request.name$());
		if (request.isType()) {
			final Frame parameterFrame = new Frame().addTypes("parameter", request.asType().type(), request.asType().getClass().getSimpleName().replace("Data", "")).addSlot("value", parameter(request, packageName));
			if (request.isList()) parameterFrame.addTypes("list");
			frame.addSlot("parameter", parameterFrame);
			frame.addSlot("parameterSignature", "value");
		}
		if (request.responseType() == Asset) frame.addSlot("method", new Frame().addSlot("download", "download"));
		else if (request.isFile()) frame.addSlot("method", new Frame().addSlot("upload", "upload"));
		else frame.addSlot("method", new Frame());
		return frame;
	}

	private static String parameter(Request request, String packageName) {
		return request.isObject() ? packageName.toLowerCase() + ".schemas." + request.asType().type() : request.asType().type();
	}

	private Template notifierTemplate() {
		return templateProvider.notifierTemplate(element);
	}

	private Template requesterTemplate() {
		return templateProvider.requesterTemplate(element);
	}

	private Template pushRequesterTemplate() {
		return templateProvider.pushRequesterTemplate(element);
	}

	private void addAccessorType(Frame frame) {
		Frame accessorType = new Frame().addSlot("value", type());
		if (element.getClass().getSimpleName().equalsIgnoreCase("display")) accessorType.addSlot("baseDisplay", "");
		if (element.getClass().getSimpleName().equalsIgnoreCase("component")) accessorType.addSlot("baseComponent", "");
		if (element.i$(Component.class)) accessorType.addSlot("component", "");
		if (element.i$(DecoratedDisplay.class)) accessorType.addSlot("abstract", "");
		frame.addSlot("accessorType", accessorType);
	}

}
