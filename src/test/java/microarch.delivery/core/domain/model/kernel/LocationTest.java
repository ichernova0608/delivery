package microarch.delivery.core.domain.model.kernel;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class LocationTest {

    @Test
    void derivedFromValueObject() {
        assertThat(Location.class.getSuperclass().getSimpleName()).isEqualTo("ValueObject");
    }

    @ParameterizedTest
    @CsvSource({ "1, 1", "5, 7", "10, 10" })
    void shouldCreateLocationWhenBothCoordinatesAreWithinAllowedRange(int x, int y) {
        var result = Location.create(x, y);

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getValue().getX()).isEqualTo(x);
        assertThat(result.getValue().getY()).isEqualTo(y);
    }

    @ParameterizedTest
    @CsvSource({ "0, 1", "11, 1", "1, 0", "1, 11" })
    void shouldReturnErrorWhenAnyCoordinateIsOutOfRange(int x, int y) {
        var result = Location.create(x, y);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getError()).isNotNull();
    }

    @Test
    void shouldBeEqualWhenBothCoordinatesMatch() {
        var first = Location.mustCreate(2, 6);
        var second = Location.mustCreate(2, 6);

        assertThat(first).isEqualTo(second);
        assertThat(first.hashCode()).isEqualTo(second.hashCode());
    }

    @ParameterizedTest
    @CsvSource({ "3, 6", "2, 7" })
    void shouldNotBeEqualWhenOnlyOneCoordinateMatches(int x, int y) {
        var first = Location.mustCreate(2, 6);
        var second = Location.mustCreate(x, y);

        assertThat(first).isNotEqualTo(second);
    }

    @Test
    void shouldNotBeEqualWhenCoordinatesAreSwapped() {
        var first = Location.mustCreate(6, 2);
        var second = Location.mustCreate(2, 6);

        assertThat(first).isNotEqualTo(second);
    }

    @Test
    void shouldCalculateDistanceUsingHorizontalAndVerticalSteps() {
        var courier = Location.mustCreate(4, 9);
        var order = Location.mustCreate(2, 6);

        assertThat(courier.distanceTo(order)).isEqualTo(5);
        assertThat(order.distanceTo(courier)).isEqualTo(5);
    }

    @Test
    void shouldHaveZeroDistanceToSameLocation() {
        var location = Location.mustCreate(4, 9);

        assertThat(location.distanceTo(location)).isZero();
    }

}
