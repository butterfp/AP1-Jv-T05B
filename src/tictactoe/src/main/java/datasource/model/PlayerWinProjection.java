package datasource.model;
import java.util.UUID;


public interface PlayerWinProjection {

    UUID getUserId();

    String getLogin();

    double getWinRatio();
}
