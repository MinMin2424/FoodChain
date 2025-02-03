package cz.cvut.fel.omo.semestralka.transaction;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class StorageModificationSecurityTransaction {

    public static List<ModificationSecurityTransaction> securityTransactions = new ArrayList<>();
}
