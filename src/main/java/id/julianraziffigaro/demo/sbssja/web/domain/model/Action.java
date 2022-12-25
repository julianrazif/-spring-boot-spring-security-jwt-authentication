package id.julianraziffigaro.demo.sbssja.web.domain.model;

public enum Action {

  ADD("ADD"),
  EDIT("EDIT"),
  DELETE("DELETE");

  private final String name;

  Action(String name) {
    this.name = name;
  }

  public static Action getByName(String name) {
    for (Action c : values()) {
      if (c.getName().equals(name)) {
        return c;
      }
    }
    return null;
  }

  public String getName() {
    return this.name;
  }
}
