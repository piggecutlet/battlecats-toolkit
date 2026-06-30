package piggecutlet.constant;

public enum ArchitectureEnum {

  // @formatter:off
  ARMEABI_V7A("armeabi-v7a"),
  ARM64_V8A("arm64-v8a"),
  X86("x86"),
  X86_64("x86_64");
  // @formatter:on

  private final String architecture;

  ArchitectureEnum(String architecture) {
    this.architecture = architecture;
  }

  @Override
  public String toString() {
    return architecture;
  }

}
