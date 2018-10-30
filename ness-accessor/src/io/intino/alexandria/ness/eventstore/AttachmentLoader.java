package io.intino.alexandria.ness.eventstore;

import io.intino.alexandria.ness.eventstore.graph.Tank;
import io.intino.ness.inl.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class AttachmentLoader {

	private static Logger logger = LoggerFactory.getLogger(AttachmentLoader.class);

	public static void loadAttachments(File inlFile, Message message) {
		final File directory = attachmentDirectoryOf(inlFile);
		try {
			for (Message.Attachment attachment : message.attachments())
				attachment.data(Files.readAllBytes(new File(directory, attachment.id()).toPath()));
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
	}

	private static File attachmentDirectoryOf(File file) {
		return new File(file.getParentFile(), file.getName().replace(Tank.INL, ""));
	}

}
