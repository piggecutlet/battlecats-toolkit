package piggecutlet.constant;

public enum LocaleEnum {

  // @formatter:off
  JP("jp.co.ponos.battlecats"),
  KR("jp.co.ponos.battlecatskr"),
  EN("jp.co.ponos.battlecatsen"),
  TW("jp.co.ponos.battlecatstw");
  // @formatter:on

  private final String packageName;

  LocaleEnum(String packageName) {
    this.packageName = packageName;
  }

  public String getPackageName() {
    return packageName;
  }

}
