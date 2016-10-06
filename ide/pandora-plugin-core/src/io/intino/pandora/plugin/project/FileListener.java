package io.intino.pandora.plugin.project;

import com.intellij.openapi.vfs.*;
import io.intino.pandora.plugin.file.PandoraFileType;
import org.jetbrains.annotations.NotNull;
import tara.intellij.lang.file.StashFileType;

import java.io.IOException;

public class FileListener implements com.intellij.openapi.components.ApplicationComponent {

	private static final String Pandora = "Pandora";
	private VirtualFileListener listener;

	@Override
	public void initComponent() {
		listener = new VirtualFileListener() {
			@Override
			public void propertyChanged(@NotNull VirtualFilePropertyEvent event) {

			}

			@Override
			public void fileDeleted(@NotNull VirtualFileEvent event) {
			}

			@Override
			public void fileMoved(@NotNull VirtualFileMoveEvent event) {
			}

			@Override
			public void contentsChanged(@NotNull VirtualFileEvent event) {

			}

			@Override
			public void fileCreated(@NotNull VirtualFileEvent event) {
				final VirtualFile file = event.getFile();
				final String stash = StashFileType.INSTANCE.getDefaultExtension();
				if (stash.equals(file.getExtension()) && Pandora.equals(file.getNameWithoutExtension()) || file.getNameWithoutExtension().endsWith("-" + Pandora)) {
					try {

						final String newName = file.getName().replace(stash, PandoraFileType.instance().getDefaultExtension()).replace("-" + Pandora, "");
						final VirtualFile old = event.getParent().findChild(newName);
						if (old != null && old.exists()) old.delete(event.getRequestor());
						file.rename(event.getRequestor(), newName);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

			@Override
			public void fileCopied(@NotNull VirtualFileCopyEvent event) {

			}

			@Override
			public void beforePropertyChange(@NotNull VirtualFilePropertyEvent event) {
			}

			@Override
			public void beforeContentsChange(@NotNull VirtualFileEvent event) {

			}

			@Override
			public void beforeFileDeletion(@NotNull VirtualFileEvent event) {

			}

			@Override
			public void beforeFileMovement(@NotNull VirtualFileMoveEvent event) {

			}
		};
		VirtualFileManager.getInstance().addVirtualFileListener(listener);
	}

	@Override
	public void disposeComponent() {
		VirtualFileManager.getInstance().removeVirtualFileListener(listener);
	}

	@NotNull
	@Override
	public String getComponentName() {
		return "Pandora File Listener";
	}
}
