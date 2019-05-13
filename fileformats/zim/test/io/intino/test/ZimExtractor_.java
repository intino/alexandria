package io.intino.test;

import io.intino.alexandria.zim.ZimExtractor;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;

public class ZimExtractor_ {

    private File tempFolder = new File("tmp");

    @Before
    public void setUp() {
        tempFolder.mkdirs();
    }

    @After
    public void tearDown() {
        purgeDirectory(tempFolder);
    }

    @Test
    public void should_extract_all_files_in_a_zim() throws IOException {
        ZimExtractor.of(new File("test-res/a.zim")).to(tempFolder);
        assertThat(filesIn(tempFolder).length).isEqualTo(9);
        assertThat(file("zim.inl").exists()).isTrue();
        assertThat(file("002eb31f-f3b3-4ba2-adfa-d758c4a55abe.jpeg").exists()).isTrue();
        assertThat(file("002eb31e-f3b3-4ba2-adfa-d758c4a55abe.jpeg").exists()).isTrue();
        assertThat(file("885d35f3-2811-42c2-a202-b6a7e4b03465.txt").exists()).isTrue();
        assertThat(file("885d35f2-2811-42c2-a202-b6a7e4b03465.txt").exists()).isTrue();
        assertThat(file("b112828f-d7d0-4694-9880-5657f570ee04.txt").exists()).isTrue();
        assertThat(file("b112828e-d7d0-4694-9880-5657f570ee04.txt").exists()).isTrue();
        assertThat(file("2c28e6e9-2c36-4d98-adbc-e55bbd9dc2e8.txt").exists()).isTrue();
        assertThat(file("2c28e6e8-2c36-4d98-adbc-e55bbd9dc2e8.txt").exists()).isTrue();
        assertThat(file("zim.inl").length()).isEqualTo(440);
        assertThat(file("002eb31f-f3b3-4ba2-adfa-d758c4a55abe.jpeg").length()).isEqualTo(10853);
        assertThat(file("002eb31e-f3b3-4ba2-adfa-d758c4a55abe.jpeg").length()).isEqualTo(10853);
        assertThat(file("885d35f3-2811-42c2-a202-b6a7e4b03465.txt").length()).isEqualTo(696);
        assertThat(file("885d35f2-2811-42c2-a202-b6a7e4b03465.txt").length()).isEqualTo(696);
        assertThat(file("b112828f-d7d0-4694-9880-5657f570ee04.txt").length()).isEqualTo(703);
        assertThat(file("b112828e-d7d0-4694-9880-5657f570ee04.txt").length()).isEqualTo(703);
        assertThat(file("2c28e6e9-2c36-4d98-adbc-e55bbd9dc2e8.txt").length()).isEqualTo(320);
        assertThat(file("2c28e6e8-2c36-4d98-adbc-e55bbd9dc2e8.txt").length()).isEqualTo(320);
        assertThat(read(file("zim.inl"))).isEqualTo("[Document]\n" + "name: name 1\n" + "file1: photo.jpeg@002eb31f-f3b3-4ba2-adfa-d758c4a55abe\n" + "file2:\n" + "\ta.txt@885d35f3-2811-42c2-a202-b6a7e4b03465\n" + "\tb.txt@b112828f-d7d0-4694-9880-5657f570ee04\n" + "\tc.txt@2c28e6e9-2c36-4d98-adbc-e55bbd9dc2e8\n" + "\n" + "\n" + "[Document]\n" + "name: name 2\n" + "file1: photo.jpeg@002eb31e-f3b3-4ba2-adfa-d758c4a55abe\n" + "file2:\n" + "\ta.txt@885d35f2-2811-42c2-a202-b6a7e4b03465\n" + "\tb.txt@b112828e-d7d0-4694-9880-5657f570ee04\n" + "\tc.txt@2c28e6e8-2c36-4d98-adbc-e55bbd9dc2e8\n" + "\n" + "\n");
        assertThat(read(file("885d35f2-2811-42c2-a202-b6a7e4b03465.txt"))).isEqualTo("Mauris ornare nibh ac libero placerat, eget eleifend risus ullamcorper. Maecenas posuere commodo dui, vitae eleifend est. Duis non augue nec ipsum cursus sagittis quis vitae leo. Nunc eu pharetra enim. Suspendisse potenti. Phasellus nunc enim, blandit vel leo ac, aliquam luctus eros. Sed et quam euismod, fringilla dui eu, rutrum lacus. Curabitur lacinia vulputate tortor vitae semper. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Ut ornare eu tellus in feugiat. Praesent quis tristique tellus, non congue nisl. Nam nec enim et purus vulputate ultrices quis eu justo. Ut id vestibulum purus. Phasellus sed felis ornare, pharetra dui eget, dapibus enim.");
        assertThat(read(file("885d35f3-2811-42c2-a202-b6a7e4b03465.txt"))).isEqualTo("Mauris ornare nibh ac libero placerat, eget eleifend risus ullamcorper. Maecenas posuere commodo dui, vitae eleifend est. Duis non augue nec ipsum cursus sagittis quis vitae leo. Nunc eu pharetra enim. Suspendisse potenti. Phasellus nunc enim, blandit vel leo ac, aliquam luctus eros. Sed et quam euismod, fringilla dui eu, rutrum lacus. Curabitur lacinia vulputate tortor vitae semper. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Ut ornare eu tellus in feugiat. Praesent quis tristique tellus, non congue nisl. Nam nec enim et purus vulputate ultrices quis eu justo. Ut id vestibulum purus. Phasellus sed felis ornare, pharetra dui eget, dapibus enim.");
        assertThat(read(file("b112828f-d7d0-4694-9880-5657f570ee04.txt"))).isEqualTo("Suspendisse varius auctor ex, sit amet fringilla erat ornare ac. Fusce orci ex, fringilla ac iaculis quis, dictum luctus erat. Vivamus ex massa, venenatis nec est vel, vehicula imperdiet augue. Aenean sem orci, placerat vitae cursus eu, sagittis quis turpis. Sed ut eros vel arcu dictum varius at vitae tortor. Phasellus consequat ultricies laoreet. Integer tristique, lectus in ultricies egestas, arcu purus molestie massa, in viverra est nunc eu magna. Sed accumsan eu turpis in porta. Morbi tincidunt sagittis volutpat. In convallis turpis mi. Sed a nisl ut ligula condimentum aliquet sit amet id risus. Donec a iaculis mauris. Praesent ullamcorper leo eget nunc bibendum, ac efficitur lectus tempus.");
        assertThat(read(file("b112828e-d7d0-4694-9880-5657f570ee04.txt"))).isEqualTo("Suspendisse varius auctor ex, sit amet fringilla erat ornare ac. Fusce orci ex, fringilla ac iaculis quis, dictum luctus erat. Vivamus ex massa, venenatis nec est vel, vehicula imperdiet augue. Aenean sem orci, placerat vitae cursus eu, sagittis quis turpis. Sed ut eros vel arcu dictum varius at vitae tortor. Phasellus consequat ultricies laoreet. Integer tristique, lectus in ultricies egestas, arcu purus molestie massa, in viverra est nunc eu magna. Sed accumsan eu turpis in porta. Morbi tincidunt sagittis volutpat. In convallis turpis mi. Sed a nisl ut ligula condimentum aliquet sit amet id risus. Donec a iaculis mauris. Praesent ullamcorper leo eget nunc bibendum, ac efficitur lectus tempus.");
        assertThat(read(file("2c28e6e9-2c36-4d98-adbc-e55bbd9dc2e8.txt"))).isEqualTo("Etiam bibendum semper imperdiet. Suspendisse at odio libero. Donec vel placerat mauris. Praesent dolor eros, consequat sed vehicula vitae, aliquet ac magna. Mauris iaculis nunc ac fringilla aliquam. Vivamus sit amet tristique neque, vitae consequat nisi. Praesent fringilla imperdiet lectus, sit amet pellentesque lacus.");
        assertThat(read(file("2c28e6e8-2c36-4d98-adbc-e55bbd9dc2e8.txt"))).isEqualTo("Etiam bibendum semper imperdiet. Suspendisse at odio libero. Donec vel placerat mauris. Praesent dolor eros, consequat sed vehicula vitae, aliquet ac magna. Mauris iaculis nunc ac fringilla aliquam. Vivamus sit amet tristique neque, vitae consequat nisi. Praesent fringilla imperdiet lectus, sit amet pellentesque lacus.");
    }

    private File file(String name) {
        return new File(tempFolder, name);
    }

    private String read(File file) throws IOException {
        return new String(Files.readAllBytes(file.toPath()));
    }

    private File[] filesIn(File tmp) {
        File[] files = tmp.listFiles();
        return files == null ? new File[0] : files;
    }

    private void purgeDirectory(File dir) {
        for (File file: requireNonNull(dir.listFiles())) {
            if (file.isDirectory()) purgeDirectory(file); else file.delete();
        }
        dir.delete();
    }
}
