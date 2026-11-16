package microarch.delivery.core.domain.model.kernel;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import libs.ddd.ValueObject;
import libs.errs.Error;
import libs.errs.Guard;
import libs.errs.Result;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;

@Embeddable
@Getter
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class Location extends ValueObject<Location> {
    private static final int MIN_VALUE = 1;
    private static final int MAX_VALUE = 10;

    @Column(name = "location_x")
    private final int x;

    @Column(name = "location_y")
    private final int y;

    public static Result<Location, Error> create(int x, int y) {
        var err = Guard.combine(Guard.againstOutOfRange(x, MIN_VALUE, MAX_VALUE, "x"),
                Guard.againstOutOfRange(y, MIN_VALUE, MAX_VALUE, "y"));
        if (err != null)
            return Result.failure(err);

        return Result.success(new Location(x, y));
    }

    public static Location mustCreate(int x, int y) {
        return create(x, y).getValueOrThrow();
    }

    @Override
    protected Iterable<Object> equalityComponents() {
        return List.of(x, y);
    }

    public int distanceTo(Location other) {
        Objects.requireNonNull(other, "other");
        return Math.abs(x - other.x) + Math.abs(y - other.y);
    }

}
