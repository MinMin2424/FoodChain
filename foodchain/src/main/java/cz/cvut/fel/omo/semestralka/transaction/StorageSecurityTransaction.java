package cz.cvut.fel.omo.semestralka.transaction;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class StorageSecurityTransaction {

    public static List<SecurityTransaction> securityTransactions = new ArrayList<>();
}
