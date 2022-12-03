package Model;

import Model.ADT.MyDictionary;
import Model.ADT.MyDictionaryInterface;
import Model.ADT.MyHeap;
import Model.ADT.MyHeapInterface;
import Model.Value.ReferenceValue;
import Model.Value.Value;

import java.util.*;
import java.util.stream.Collectors;

public class GarbageCollector {
    public List<ProgramState> collectGarbage(List<ProgramState> programStates) {
        Set<Integer> combinedAddresses = new HashSet<>();
        
        for(ProgramState state: programStates){

            MyHeap<Integer, Value> heap = (MyHeap<Integer, Value>) state.getHeap();
            MyDictionary<String, Value> symTable = state.getSymbolTable();
            Collection<Value> symTableValues = symTable.getDictionary().values();
            Collection<Value> heapValues = heap.getHeap().values();

            List<Integer> symTableAddresses = getAddressesFromCollection(symTableValues);
            List<Integer> heapAddresses = getAddressesFromCollection(heapValues);
            combinedAddresses.addAll(symTableAddresses);
            combinedAddresses.addAll(heapAddresses);
        }

        List<Integer> addressesToKeep = new ArrayList<>(combinedAddresses);
        
        for(ProgramState state: programStates){

            MyHeap<Integer, Value> heap = (MyHeap<Integer, Value>) state.getHeap();
            Map<Integer,Value> oldHeapContent = heap.getHeap();
            Map<Integer, Value> newHeapContent = unsafeGarbageCollector(addressesToKeep, oldHeapContent);
            heap.setHeap((HashMap<Integer, Value>) newHeapContent);
            
        }

        return programStates;
    }
    /**
     * Filters only the addresses that are not referenced in the symbol table or the heap
     */
    public Map<Integer,Value> unsafeGarbageCollector(List<Integer> addressesToKeep, Map<Integer,Value> heap){
        return heap.entrySet().stream() 
                                .filter(e->addressesToKeep.contains(e.getKey()))
                                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
    public List<Integer> getAddressesFromCollection(Collection<Value> symTableValues){
        return symTableValues.stream()                    
                                .filter(v-> v instanceof ReferenceValue)
                                .map(v-> {ReferenceValue v1 = (ReferenceValue) v; return v1.getAddress();})
                                .collect(Collectors.toList());}
    }
//     public List<Integer> getAddressesFromHeap(Collection<Value> heapValues){
//         return heapValues.stream()
//                                 .filter(v-> v instanceof ReferenceValue)
//                                 .map(v-> {ReferenceValue v1 = (ReferenceValue) v; return v1.getAddress();})
//                                 .collect(Collectors.toList());}
// }
