package io.intino.konos.builder.codegeneration.services.ui.passiveview;

import io.intino.konos.builder.codegeneration.Settings;
import io.intino.konos.builder.codegeneration.services.ui.ElementRenderer;
import io.intino.konos.model.graph.Display;
import io.intino.konos.model.graph.PassiveView;
import io.intino.konos.model.graph.PassiveView.Notification;
import io.intino.konos.model.graph.PassiveView.Request;
import org.siani.itrules.Template;
import org.siani.itrules.model.Frame;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import static cottons.utils.StringHelper.snakeCaseToCamelCase;
import static io.intino.konos.builder.helpers.Commons.writeFrame;
import static io.intino.konos.model.graph.PassiveView.Request.ResponseType.Asset;

public abstract class PassiveViewRenderer<C extends PassiveView> extends ElementRenderer<C> {

	protected PassiveViewRenderer(Settings settings, C element) {
		super(settings, element);
	}

	@Override
	public Frame buildFrame() {
		Frame frame = super.buildFrame();
		String type = type();
		frame.addSlot("type", type);
		if (!type.equalsIgnoreCase("display")) frame.addSlot("packageType", type.toLowerCase());
		frame.addSlot("parentType", parentType());
		frame.addSlot("name", clean(element.name$()));
		frame.addSlot("notification", framesOfNotifications(element.notificationList()));
		frame.addSlot("request", framesOfRequests(element.requestList()));
		return frame;
	}

	protected void createPassiveViewFiles(Frame frame) {
		writeNotifier(frame);
		writeRequester(frame);
	}

	private void writeRequester(Frame frame) {
		writeRequester(element, frame);
	}

	protected void writeRequester(PassiveView element, Frame frame) {
		writeFrame(new File(gen(), Requesters), snakeCaseToCamelCase(element.name$() + (Arrays.asList(frame.types()).contains("accessible") ? "Proxy" : "") + "Requester"), displayRequesterTemplate().format(frame));
	}

	private void writeNotifier(Frame frame) {
		writeNotifier(element, frame);
	}

	protected void writeNotifier(PassiveView element, Frame frame) {
		writeFrame(new File(gen(), Notifiers), snakeCaseToCamelCase(element.name$() + (Arrays.asList(frame.types()).contains("accessible") ? "Proxy" : "") + "Notifier"), displayNotifierTemplate().format(frame));
	}

	private Template displayRequesterTemplate() {
		return setup(PassiveViewRequesterTemplate.create());
	}

	private Template displayNotifierTemplate() {
		return setup(PassiveViewNotifierTemplate.create());
	}

	private String type() {
		return typeOf(element.a$(Display.class));
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
		return requests.stream().map(r -> frameOf(r, packageName())).toArray(Frame[]::new);
	}

	public static Frame frameOf(Request request, String packageName) {
		final Frame frame = new Frame().addTypes("request");
		if (request.responseType().equals(Asset)) frame.addTypes("asset");
		frame.addSlot("name", request.name$());
		if (request.isType()) {
			final Frame parameterFrame = new Frame().addTypes("parameter", request.asType().type(), request.asType().getClass().getSimpleName().replace("Data", "")).addSlot("value", parameter(request, packageName));
			if (request.isList()) parameterFrame.addTypes("list");
			frame.addSlot("parameter", parameterFrame);
		}
		return frame;
	}

	private static String parameter(Display.Request request, String packageName) {
		return request.isObject() ? packageName.toLowerCase() + ".schemas." + request.asType().type() : request.asType().type();
	}

}
