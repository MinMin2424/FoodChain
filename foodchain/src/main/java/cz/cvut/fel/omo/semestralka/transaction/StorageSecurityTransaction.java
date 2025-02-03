package cz.cvut.fel.omo.semestralka.transaction;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class StorageSecurityTransaction {

    public static List<SecurityTransaction> securityTransactions = new ArrayList<>();
}
