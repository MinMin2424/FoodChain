package cz.cvut.fel.omo.semestralka.transaction;


import cz.cvut.fel.omo.semestralka.model.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class PartiesTransaction {
    private String productName;
    private String personName;
    private long duration;
}
