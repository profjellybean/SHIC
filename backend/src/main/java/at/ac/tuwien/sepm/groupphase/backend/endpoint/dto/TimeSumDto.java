package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import java.time.LocalDate;
import java.util.Objects;

public class TimeSumDto {
    private LocalDate date;
    private double sum;

    public TimeSumDto() {
    }

    public double getSum() {
        return sum;
    }

    public TimeSumDto(double sum, LocalDate date) {
        this.date = date;
        this.sum = sum;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TimeSumDto that = (TimeSumDto) o;
        return Double.compare(that.sum, sum) == 0 && Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, sum);
    }

    @Override
    public String toString() {
        return "TimeSumDto{"
            +
            "date=" + date
            +
            ", sum=" + sum
            +
            '}';
    }
}

