package arbolesb;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Nodo {
    /**
     * El orden del arbol.
     */
    private static int n;
    
    /**
     * Arreglo con los elementos almacendaos en el nodo. De tamaño 2n.
     */
    private int[] info;
    
    /**
     * Variable con la cantidad de elementos ingresados en el arreglo info.
     */
    private int contInfo;
    
    /**
     * Referencia al padre del nodo.
     */
    private Nodo padre;
    
    /**
     * Arreglo de referencias a los hijos. El primer elmento apunta al nodo de 
     * los menores al primer elemento, el segundo a los mayores del primero y
     * menores al segundo (si hay segundo).
     * <p>
     * De tamaño 2n+1 (+1 extra).
     */
    private Nodo[] hijos;

    
    public static void debug(){
        System.out.println(Nodo.raiz.hijos[1].EncontrarCuantosHijos());
    }
    
    
    /**
     * Constructor de un nodo raiz, con un solo elemento inicial
     * 
     * @param elemento Numero con el cual inicia el nodo.
     * @throws Exception orden del arbol menor que cero o no inicializado.
     */
    public Nodo(int elemento) throws Exception {
        if(n<=0) throw new Exception("Orden del arbol menor a cero");
        
        this.info = new int[2*n];
        this.hijos = new Nodo[2*n + 2];
        this.contInfo = 1;
        
        this.info[0] = elemento;
    }
    
    /**
     * Constructor de un nodo que no es raiz, con n elementos iniciales
     * 
     * @param elementos arreglo con los n elementos iniciales del nodo.
     * @param padre referencia del nodo padre del nodo a crear.
     * @throws Exception Orden del arbol menor que cero o no inicializado.
     * @throws Exception Numero de elementos ingresados para crear el nodo 
     * diferente de n.
     */
    public Nodo(int[] elementos, Nodo padre) throws Exception{
        if(n<=0) throw new Exception("Orden del arbol menor a cero");
        if(elementos.length!=n) throw new Exception("Numero de elementos "
                + "iniciales del nodo diferente de n");
        
        this.info = new int[2*n];
        this.hijos = new Nodo[2*n + 2];
        
        this.contInfo = n;
        
        System.arraycopy(elementos, 0, this.info, 0, n);
//        Lo mismo que:
//        for (int i = 0; i < n; i++) {
//            this.info[i] = elementos[i];
//        }
        
        this.padre = padre;
    }
    
    /**
     * Metodo para obtener el numero de hijos del nodo.
     * @return El numero de hijos del nodo.
     */
    public int EncontrarCuantosHijos(){
        int cont = 0;
        
        boolean end = false;
        for (int i = 0; i < hijos.length && !end; i++) {
            if(hijos[i]==null) end=true;
            else cont++;
        }
        
        return cont;
    }
    
    /**
     * Metodo llamado cuando se quiere insertar un elemento en el nodo (o en 
     * uno de sus hijos).
     * 
     * @param in Elemento a insertar.
     */
    public void Insertar(int in) throws Exception{
        if (EncontrarCuantosHijos()==0) { //si es hoja
            if(contInfo!=2*n){ //no esta lleno
                int i; boolean found = false;
                for (i = 0; i < contInfo && !found; i++) {
                    if(info[i]>in) found = true;
                }
                if(found){
                    System.arraycopy(info, i-1, info, i, info.length-i);
                    info[i-1]=in;
                } else {
                    info[i]=in;
                }
                contInfo++;
            } else {
                Split(in);
            }
        } else {
            int i; boolean found = false;
            for (i = 0; i < contInfo && !found; i++) {
                if(info[i]>in) found = true;
            }
            if(!found){
                hijos[i].Insertar(in);
            } else {
                hijos[i-1].Insertar(in);
            }         
        }
    }
    
    /**
     * Metodo para meter un numero en el nodo en orden y mover los contenidos 
     * e hijos a la derecha. Tambien se ingresan los nodos izq y der en donde se
     * ingresa el elemento
     * @param x Elemento a ingresar
     * @param l Nodo izuierdo a ingresar
     * @param r Nodo derecho a ingresar
     */
    @SuppressWarnings("empty-statement")
    private void IngresarYMover(int x, Nodo l, Nodo r){
        int i;
        for (i = 0; i < contInfo && info[i]<x; i++) {
        }//ingreso en i
        
        
        int tmp = info[i];
        info[i] = x;
        contInfo++;
        
        for (int j = i+1; j < contInfo; j++) { //muevo el contenido            
            int tmp2 = info[j];
            info[j]=tmp;
            tmp=tmp2;
        }
        
        hijos[i]=l;
        
        Nodo Ntmp = hijos[i+1];
        hijos[i+1] = r;
        
        for (int j = i+2; j < contInfo+1; j++) {
            Nodo Ntmp2 = hijos[j];
            hijos[j]=Ntmp;
            Ntmp=Ntmp2;
        }
        
        l.padre=this;
        r.padre=this;
    }
    
    
    /**
     * Metodo para mover el contenido de Hijos una posicion a la derecha desde
     * la pasicion donde se encuentre el hijo de los argumentos.
     * 
     * @param hijo Nodo a buscar en el arreglo hijos
     * @return La posicion en que se mueve el arreglo
     * @throws Exception Cuando el hijo dado en argumentos no se encuentra en el
     * arreglo hijos
     */
    private int MoverHijosADerecha (Nodo hijo) throws Exception{
        int i; boolean found=false;
        for (i = 0; i < contInfo+1 && !found; i++) {
            if (hijos[i]==hijo) {
                found=true;
            }
        }//encontrado en i-1
        
        if (!found) {
            throw new Exception("Hijo not found by MoverHijosADerecha");
        }
        
        Nodo tmp = null;
        for (int j = i; j < contInfo+2; j++) {
            Nodo tmp2 = hijos[j];
            hijos[j]=tmp;
            tmp=tmp2;
        }
        
        return i-1;
    }
    
    /**
     * Metodo para partir el nodo en dos, cada nuevo nodo con n elementos, 
     * el padre agrega a los hijos y agrega el elemento del medio al contenido.
     * 
     * @param in elemento que se esta agregando.
     */
    @SuppressWarnings("empty-statement")
    private void Split(int in) throws Exception{
        /*
         * Guarda en el arreglo tmp el contenido del nodo a partir mas el 
         * elemento ingresado.
         * 
         * 0->n: Nodo Izq
         * n: Elemento de la mitad (que debe subir)
         * n+1->2n: Nodo Der
         */
        //TODO: Refactor this in to a method
        int[] tmp = new int[info.length+1];
        int i = 0; boolean placed = false;
        while(i<tmp.length-1 && !placed){
            if(info[i] > in){
                placed=true;
            } else {
                tmp[i]=info[i];
                i++;
            }
        }
        tmp[i]=in;
        i++;
        while(i<tmp.length) { //si falta algo en info
            tmp[i]=info[i-1];
            i++;
        }
        
        
        //crear nodo izq (L)
        int[] arrL = new int[n];
        System.arraycopy(tmp, 0, arrL, 0, n);
        Nodo l = new Nodo(arrL, padre);
        
        //crear nodo der
        int[] arrR = new int[n];
        System.arraycopy(tmp, n+1, arrR, 0, n);
        Nodo r = new Nodo(arrR, padre);
        
        if (EncontrarCuantosHijos()!=0) { //SI tiene hijos
            System.arraycopy(hijos, 0, l.hijos, 0, n+1);
            for (Nodo nodo : l.hijos) {
                if(nodo!=null) nodo.padre = l;
            }
            
            System.arraycopy(hijos, n+1, r.hijos, 0, n+1);
            for (Nodo nodo : r.hijos) {
                if(nodo!=null) nodo.padre = r;
            }
        } 
        
        if (this.padre==null) { //si es raiz
            Nodo.raiz = new Nodo(tmp[n]);
            l.padre=Nodo.raiz;
            r.padre=Nodo.raiz;
            
            Nodo.raiz.hijos[0] = l;
            Nodo.raiz.hijos[1] = r;
        } else { //Si no es raiz 
            
            if (this.padre.contInfo == 2*n) {//si el padre esta lleno
                int pos = padre.MoverHijosADerecha(this);
                padre.hijos[pos]=l;
                padre.hijos[pos+1]=r;
                padre.Split(tmp[n]);
            } else {
                padre.IngresarYMover(tmp[n], l, r);
            }
        }
    }
    
    /**
     * Metodo que busca un elemento en el nodo y en sus hijos.
     * 
     * @param elemento elemento a buscar, null si no lo encuentra.
     * @return Retorna el nodo en que se encuentra el elemento.
     */
    public Nodo Buscar(int elemento){
        int cur = 0;
        while (cur<contInfo && info[cur]<elemento){
            cur++;
        }
        if(cur<info.length && info[cur]==elemento) return this;
        else{
            //System.out.println("entro " + cur);
            
            if(hijos[cur]==null){
                return null;
            } else {
                return hijos[cur].Buscar(elemento);
            }
        }
    }
    
    /**
     * Elimina un elemento del arreglo info
     * @param elemento Elemento a borrar
     */
    private void BorrarDeInfo(int elemento){
        //se busca el elemento en el arreglo
        boolean found = false;
        int i;
        for (i = 0; i < contInfo && !found; i++) {
            if (info[i] == elemento) {
                found = true;
            }
        }
        while (i<info.length-1) {            
            int tmp = info[i];
            info[i]=info[i+1];
            info[i+1]=tmp;
            
            i++;
        }
        
    }
    
    /**
     * Metodo para imprimir el contenido del nodo.
     */
    public void ImprimirContenido(){
        System.out.print("Contenido Nodo:");
        for (int i = 0; i < contInfo; i++) {
            System.out.print(" " + info[i]);
        }
        System.out.println(" En nivel " + EncontrarNivel());
    }
    
    /**
     * Metodo para encontrar el nivel del nodo en el arbol.
     * 
     * @return El nivel del nodo en el arbol.
     */
    public int EncontrarNivel(){
        if(this == Nodo.raiz)  return 0;
        else return this.padre.EncontrarNivel()+1;
    }
    
    //geters y seters
    public static int getN() {
        return n;
    }
    public static void setN(int n) {
        Nodo.n = n;
    }
    /**
     * Geter de la cantidad de elementos ingresados en el arreglo Info.
     * 
     * @return La cantidad de elementos ingresados.
     */
    public int getContInfo() {
        return contInfo;
    }
    /**
     * Seter de la cantidad de elementos ingresados en el arreglo Info.
     * 
     * @param contInfo La cantidad de elementos ingresados.
     */
    public void setContInfo(int contInfo) {
        this.contInfo = contInfo;
    }
    
    /**
     * Metodo para obtener el contenido de info en la posicion i.
     * 
     * @param i Posicion del arreglo a obtener.
     * @return El contenido del arreglo info en la posicion i.
     */
    public int getInfo(int i){
        return info[i];
    }
    
    /**
     * Metodo que retorna el contenido de info como un string
     * @return Un string con el contenido de info concatenado
     */
    public String getInfoString(){
        String out = "";
        for (int i = 0; i < this.getContInfo(); i++) {
            out += this.info[i] + " ";
        }
        
        return out;
    }
    
    public Nodo[] getHijos() {
        return hijos;
    }
    
    public Nodo getHijos(int i){
        return hijos[i];
    }
    
    
    //------Estaticos--------
    /**
     * Lista con los elementos insertados al arbol
     */
    private static ArrayList<Integer> elementosIngresados;
    
    /**
     * Nodo raiz del arbol.
     */
    private static Nodo raiz;
    
    /**
     * Metodo para incertar un elemento en el arbol.
     * 
     * @param valor valor a insertar.
     * @throws Exception orden del arbol menor que cero o no inicializado.
     */
    public static void InsertarEnArbol(int valor) throws Exception{
        if(raiz==null) {
            Nodo.raiz = new Nodo(valor);
        } else {
            Nodo.raiz.Insertar(valor);
        }
        
        if (elementosIngresados == null) {
            elementosIngresados = new ArrayList<>();
            elementosIngresados.add(valor);
        } else {
            elementosIngresados.add(valor);
        }
    }
    
    /**
     * Metodo para crear un arbol apartir de un ArrayList
     * 
     * @param list ArrayList con los elementos a insertar
     * @throws Exception En caso de que la insercion falle.
     */
    private static void CrearArbolDesdeLista(ArrayList list) throws Exception{
        for (int i = 0; i < list.size(); i++) {
            InsertarEnArbol((int) list.get(i));
        }
    }
    
    /**
     * Metodo para buscar un elemento en el arbol. Retorna true o false si lo
     * encuentra o no.
     * 
     * @param valor valor a buscar
     * @return True si lo encuentra, False si no.
     */
    public static boolean EstaEnArbol(int valor){
        Nodo result = BuscarEnArbol(valor);
        
        if(result == null){
            //System.out.println("No se encontro el valor: " + valor);
            return false;
        }
        else {
//            String out = "El elemento " + valor + " se encontro en el nivel " +
//                    result.EncontrarNivel() + " en el nodo con valores: ";
//            
//            for (int i = 0; i < result.contInfo; i++) {
//                out += result.info[i] + " ";
//            }
//            
//            System.out.println(out);
            
            return true;
        }
    }
    
    /**
     * Metodo para buscar un elemento en el arbol
     * @param val Elemento a buscar
     * @return El nodo en que se encuentra el valor
     */
    public static Nodo BuscarEnArbol(int val){
        Nodo result;
        if(Nodo.raiz==null) result = null;
        else result = Nodo.raiz.Buscar(val);
        
        return result;
    }
    
    /**
     * Metodo para imprimir el arbol. Este recorre el arbol con un BFS e imprime
     * el contenido del nodo
     */
    public static void ImprimirArbol() {
        ArrayDeque<Nodo> q = new ArrayDeque<>();
        q.add(Nodo.raiz);
        System.out.println("Raiz:");
        Nodo.raiz.ImprimirContenido();
        while (!q.isEmpty()) {            
            Nodo cur = q.remove();
            for (int i = 0; i < 2*n+1; i++) {
                System.out.print("Hijo " + (i+1) + ":");
                if(cur.hijos[i] == null){
                    System.out.println(" vacio");
                } else {
                    System.out.println("");
                    cur.hijos[i].ImprimirContenido();
                    q.add(cur.hijos[i]);
                }
            }
        }
    }
    
    /**
     * Elimina por completo el arbol
     */
    public static void BorrarArbol(){
        Nodo.raiz = null;
        elementosIngresados.clear();
    }
    
    /**
     * Metodo para retirar o eliminar un elemento del arbol.
     * 
     * @param valor valor a eliminar.
     */
    public static void EliminarEnArbol(int valor) throws Exception{
        if(elementosIngresados.contains(valor) == false) throw new Exception("valor a eliminar no encontrado");
        try {
            int i = elementosIngresados.indexOf(valor);
            elementosIngresados.remove(i);
            ArrayList<Integer> l = new ArrayList<>(elementosIngresados);
            
            Nodo.BorrarArbol();
            
            CrearArbolDesdeLista(l);
        } catch (Exception ex) {
            System.out.println("Error eliminando: " + valor);
            Logger.getLogger(Nodo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Metodo para obtener el numero de elementos ingrsados en el arbol
     * @return El numero de elementos en el arbol
     */
    public static int getNumElementos(){
        if (elementosIngresados==null) {
            return 0;
        } else{
            return elementosIngresados.size();
        }
    }

    /**
     * Seter de la raiz del arbol.
     * @param raiz la nueva raiz.
     */
    public static void setRaiz(Nodo raiz) {
        Nodo.raiz = raiz;
    }
    /**
     * Getter de la raiz del arbol
     * @return raiz del arbol
     */
    public static Nodo getRaiz() {
        return raiz;
    }

    @Override
    public String toString() {
        String str = "";
        for (int i = 0; i < contInfo; i++) {
            str += info[i] + " ";
        }
        
        return str; //To change body of generated methods, choose Tools | Templates.
    }
    
    public static int getMaxLevel(){
        return raiz.getMaxLevelDown();
    }

    /**
     * Metodo para encontrar la cantidad maxima de niveles en el subarbol que 
     * comienza en el nodo en que se llame el metodo.
     * 
     * @return La cantidad maxima de niveles en el subarbol
     */
    public int getMaxLevelDown() {
        if(this.hijos!=null){
            int max = 0;
            for (Nodo nodo : hijos) {
                if(nodo!=null) max = Math.max(max, nodo.getMaxLevelDown());
            }
            if(max!=0) return max;
        }
        
        return EncontrarNivel();
    }
    
    
    /**
     * Metodo para encontrar el maximo numero posible de hojas en un nivel del arbol
     * @param l Nivel a encontrar el numero de hojas
     * @return En numero maximo posible de hojas
     */
    public static int findMaxNumLeaves(int l){
        return (int) Math.pow(getN()*2+1, l);
    }
}
