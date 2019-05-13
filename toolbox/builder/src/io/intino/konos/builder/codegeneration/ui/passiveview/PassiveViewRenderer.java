package io.intino.konos.builder.codegeneration.ui.passiveview;

import io.intino.itrules.Frame;
import io.intino.itrules.FrameBuilder;
import io.intino.itrules.Template;
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

import java.io.File;
import java.util.List;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;
import static io.intino.konos.model.graph.PassiveView.Request.ResponseType.Asset;

public abstract class PassiveViewRenderer<C extends PassiveView> extends ElementRenderer<C> {

	protected PassiveViewRenderer(Settings settings, C element, TemplateProvider templateProvider, Target target) {
		super(settings, element, templateProvider, target);
	}

	@Override
	public FrameBuilder frameBuilder() {
		FrameBuilder result = super.frameBuilder();
		FrameBuilder extensionFrame = extensionFrame();
		String type = type();
		result.add("id", shortId(element));
		result.add("type", type);
		addAccessorType(result);
		result.add("parentType", extensionFrame);
		result.add("import", extensionFrame);
		if (!type.equalsIgnoreCase("display")) result.add("packageType", type.toLowerCase());
		result.add("packageTypeRelativeDirectory", typeOf(element).equalsIgnoreCase("display") ? "" : "../");
		result.add("name", nameOf(element));
		result.add("notification", framesOfNotifications(element.notificationList()));
		result.add("request", framesOfRequests(element.requestList()));
		return result;
	}

	protected void createPassiveViewFiles(FrameBuilder elementBuilder) {
		Frame frame = elementBuilder.toFrame();
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
		writeFrame(new File(gen(), format(Requesters)), snakeCaseToCamelCase(element.name$() + (isAccessible(frame) ? "Proxy" : "") + "Requester"), displayRequesterTemplate().render(frame));
	}

	private void writePushRequester(Frame frame) {
		writePushRequester(element, frame);
	}

	protected void writePushRequester(PassiveView element, Frame frame) {
		Template template = displayPushRequesterTemplate();
		boolean accessible = isAccessible(frame);
		if (accessible || template == null) return;
		writeFrame(new File(gen(), format(Requesters)), snakeCaseToCamelCase(element.name$() + "PushRequester"), template.render(frame));
	}

	private boolean isAccessible(Frame frame) {
		return frame.is("accessible");
	}

	private void writeNotifier(Frame frame) {
		writeNotifier(element, frame);
	}

	protected void writeNotifier(PassiveView element, Frame frame) {
		writeFrame(new File(gen(), format(Notifiers)), snakeCaseToCamelCase(element.name$() + (isAccessible(frame) ? "Proxy" : "") + "Notifier"), displayNotifierTemplate().render(frame));
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

	private FrameBuilder extensionFrame() {
		FrameBuilder result = new FrameBuilder().add(type(), "").add("value", type()).add("type", type());
		if (element.isExtensionOf()) {
			result.add("extensionOf", "extensionOf");
			result.add("parent", element.asExtensionOf().parentView().name$());
		}
		return result;
	}

	private Frame[] framesOfNotifications(List<Notification> notifications) {
		return notifications.stream().map(this::frameOf).toArray(Frame[]::new);
	}

	private Frame frameOf(Notification notification) {
		final FrameBuilder result = new FrameBuilder().add("notification");
		result.add("name", notification.name$());
		result.add("target", notification.to().name());
		if (notification.isType()) {
			final FrameBuilder parameterFrame = new FrameBuilder().add("parameter").add(notification.asType().type()).add(notification.asType().getClass().getSimpleName().replace("Data", "")).add("value", notification.asType().type());
			if (notification.isList()) parameterFrame.add("list");
			result.add("parameter", parameterFrame);
		}
		return result.toFrame();
	}

	private Frame[] framesOfRequests(List<Request> requests) {
		return requests.stream().map(r -> frameOf(element, r, packageName())).toArray(Frame[]::new);
	}

	public static Frame frameOf(Layer element, Request request, String packageName) {
		final FrameBuilder result = new FrameBuilder().add("request");
		result.add("display", element.name$());
		if (request.responseType().equals(Asset)) result.add("asset");
		if (request.isFile()) result.add("file");
		result.add("name", request.name$());
		if (request.isType()) {
			final FrameBuilder parameterFrame = new FrameBuilder().add("parameter").add(request.asType().type()).add(request.asType().getClass().getSimpleName().replace("Data", "")).add("value", parameter(request, packageName));
			if (request.isList()) parameterFrame.add("list");
			result.add("parameter", parameterFrame);
			result.add("parameterSignature", "value");
		}
		if (request.responseType() == Asset) result.add("method", new FrameBuilder().add("download", "download"));
		else if (request.isFile()) result.add("method", new FrameBuilder().add("upload", "upload"));
		else result.add("method", new FrameBuilder());
		return result.toFrame();
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

	private void addAccessorType(FrameBuilder builder) {
		FrameBuilder accessorType = new FrameBuilder().add("value", type());
		if (element.getClass().getSimpleName().equalsIgnoreCase("display")) accessorType.add("baseDisplay", "");
		if (element.getClass().getSimpleName().equalsIgnoreCase("component")) accessorType.add("baseComponent", "");
		if (element.i$(Component.class)) accessorType.add("component", "");
		if (element.i$(DecoratedDisplay.class)) accessorType.add("abstract", "");
		builder.add("accessorType", accessorType);
	}

}
