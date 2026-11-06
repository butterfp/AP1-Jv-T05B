package domain.model;


public record SignUpRequest(String login, String password) {


    public SignUpRequest {
        if (login != null) {
            login = login.trim();
        }
    }
}
