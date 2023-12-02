package io.intino.alexandria.ui.displays.components.actionable;

public class SignInfo {
    private String secret;
    private String email;
    private String company;
    private boolean canSetup;

    public String secret() {
        return secret;
    }

    public SignInfo secret(String signSecret) {
        this.secret = signSecret;
        return this;
    }

    public String email() {
        return email;
    }

    public SignInfo email(String signEmail) {
        this.email = signEmail;
        return this;
    }

    public String company() {
        return company;
    }

    public SignInfo company(String signCompany) {
        this.company = signCompany;
        return this;
    }

    public boolean canSetup() {
        return canSetup;
    }

    public SignInfo canSetup(boolean canSetup) {
        this.canSetup = canSetup;
        return this;
    }
}
