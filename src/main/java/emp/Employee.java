package emp;

import java.time.LocalDate;

public class Employee {
    private Integer id;
    private LocalDate dateFrom;
    private LocalDate dateTo;

    public Employee(Integer id, LocalDate dateFrom, LocalDate dateTo) {
        this.id = id;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getDateFrom() {
        return this.dateFrom;
    }

    public void setDateFrom(LocalDate dateFrom) {
        this.dateFrom = dateFrom;
    }

    public LocalDate getDateTo() {
        return this.dateTo;
    }

    public void setDateTo(LocalDate dateTo) {
        this.dateTo = dateTo;
    }
}
