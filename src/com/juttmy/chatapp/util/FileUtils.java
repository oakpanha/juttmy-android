package com.juttmy.chatapp.util;

import java.io.FileDescriptor;

public class FileUtils {

  public static native int getFileDescriptorOwner(FileDescriptor fileDescriptor);

}
