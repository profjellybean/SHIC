package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import java.time.LocalDate;

public class TimeSumDto {
    private LocalDate date;
    private double sum;

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
}

