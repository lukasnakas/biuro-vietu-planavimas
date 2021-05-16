import { shallow } from "enzyme";
import Header from "../Header";

describe("<Header />", () => {
  const headerWrapper = shallow(<Header />);

  test("Should have Link on logo to return to home page", () => {
    const linkWrapper = headerWrapper.find("Link");
    expect(linkWrapper.exists()).toBeTruthy();
    expect(linkWrapper.prop("to")).toBe("/home");
    expect(linkWrapper.prop("title")).toBe("Visma Mandate Service");
  });

  test("Should render Logo", () => {
    const logoWrapper = headerWrapper.find("Link").childAt(0);
    expect(logoWrapper.exists()).toBeTruthy();
    expect(logoWrapper.prop("title")).toBe("Visma Mandate Service");
    expect(logoWrapper.type().render().type).toBe("svg");
    expect(logoWrapper.type().render().props.children).toBe(
      "visma-logo-white.svg"
    );
  });
});
