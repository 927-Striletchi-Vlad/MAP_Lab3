// package View;

// import Controller.Controller;
// import Exception.MyException;
// import Model.ADT.MyDictionaryInterface;
// import Model.ADT.MyListInterface;
// import Model.ADT.MyStackInterface;
// import Model.ProgramState;
// import Model.Statement.IStmt;
// import Model.Type.BoolType;
// import Model.Type.IntType;
// import Model.Value.BoolValue;
// import Model.Value.IntValue;
// import Model.Value.Value;

// public class View {
//     Controller controller;

//     public View(Controller controller) {
//         this.controller = controller;
//     }

//     public void run(){
//         System.out.println("==================== RUN ====================");
//         try{
//             controller.allStep();
//             MyListInterface<Value> output = controller.getCurrentState().getOutput();
//             for(Value v:output.getList()){
//                 if (v.getType().equals(new BoolType())){
//                     System.out.println(((BoolValue)v).getValue());
//                 }
//                 if (v.getType().equals(new IntType())){
//                     System.out.println(((IntValue)v).getValue());
//                 }
//             }

//         }
//         catch (MyException e){
//             System.out.println(e.getMessage());
//         }
//     }

//     private void debugCycle() throws MyException{
//         try {
//             ProgramState state = controller.getCurrentState();
//             System.out.println(state.toString());
//             controller.oneStep(state);
//         } catch (MyException e) {
//             throw e;
//         }
//     }

//     public void debug(){
//         System.out.println("==================== DEBUG ====================");
//         while(!(controller.getCurrentState().getStack().isEmpty())) {
//             try {
//                 debugCycle();
//             } catch (MyException e) {
//                 System.out.println(e.getMessage());
//                 return;
//             }
//         }
//         try {
//             debugCycle();
//         } catch (MyException e) {
//             System.out.println(e.getMessage());
//         }
//     }


// }
