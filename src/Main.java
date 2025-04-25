import java.util.*;

class Voto{

    private int id;
    private int votanteID;
    private int candidatoID;
    private String timestamp;

    public Voto(int id, int votanteID, int candidatoID, String timestamp){
        this.id = id;
        this.votanteID = votanteID;
        this.candidatoID = candidatoID;
        this.timestamp = timestamp;
    }

    public int getId(){
        return id;
    }
    public int getVotanteID(){
        return votanteID;
    }
    public int getCandidatoID(){
        return candidatoID;

    }
    public String getTimestamp(){
        return timestamp;
    }

    public void setId(int id){
        this.id = id;
    }
    public void setVotanteID(int votanteID){
        this.votanteID = votanteID;
    }
    public void setCandidatoID(int candidatoID){
        this.candidatoID = candidatoID;
    }
    public void setTimestamp(String timestamp){
        this.timestamp = timestamp;
    }
}

class Candidato{
    private int id;
    private String nombre;
    private String partido;
    private Queue <Voto> votosRecibidos;

    public Candidato(int id, String nombre, String partido){
        this.id = id;
        this.nombre = nombre;
        this.partido = partido;
        votosRecibidos = new LinkedList();
    }

    public int getId(){
        return id;
    }
    public String getNombre(){
        return nombre;
    }
    public String getPartido(){
        return partido;
    }
    public Queue<Voto> getVotosRecibidos() {
        return votosRecibidos;
    }
    public void setId(int id){
        this.id = id;
    }
    public void setNombre(String nombre){
        this.nombre = nombre;
    }
    public void setPartido(String partido){
        this.partido = partido;
    }
    public void setVotosRecibidos(Queue<Voto> votos) {
        this.votosRecibidos = votos;
    }

    public void agregarVoto(Voto v){
        votosRecibidos.add(v);
    }
}

class Votante{
    private int id;
    private String nombre;
    private boolean yaVoto = false;
    public Votante(int id, String nombre){
        this.id=id;
        this.nombre=nombre;
        this.yaVoto=yaVoto;
    }
    public int getId(){
        return id;
    }
    public String getNombre(){
        return nombre;
    }
    public boolean getYaVoto(){
        return yaVoto;
    }
    public void setId(int id){
        this.id=id;
    }
    public void setNombre(String nombre){
        this.nombre=nombre;
    }
    public void setYaVoto(boolean yaVoto){
        this.yaVoto=yaVoto;
    }
    public void marcarComoVotado(){
        this.yaVoto=true;
    }
}

class UrnaElectoral{
    private LinkedList<Candidato>listaCandidato;
    private Stack <Voto>historialVotos;
    private Queue <Voto>votosReportados;
    private int idCounter = 1;
    public UrnaElectoral(){
        listaCandidato = new LinkedList<Candidato>();
        historialVotos = new Stack<Voto>();
        votosReportados= new LinkedList<Voto>();
    }

    public LinkedList<Candidato> getListaCandidato(){
        return listaCandidato;
    }

    public boolean verificarVotante(Votante votante){
        return votante.getYaVoto();}

    public void registrarVoto(Votante votante, int candidatoID){
        if(verificarVotante(votante)){
            System.out.println("Ya voto");
            return;
        }
        Candidato c=null;
        for(int i=0; i<listaCandidato.size(); i++){
            if(listaCandidato.get(i).getId()==candidatoID){
                c=listaCandidato.get(i);
                break;
            }
        }
        if(c==null){
            System.out.println("Candidato no encrontrado");
            return;
        }
        String timestamp = new java.util.Date().toString();
        Voto nvoto= new Voto(idCounter,votante.getId(),candidatoID,timestamp);
        idCounter++;
        c.agregarVoto(nvoto);
        historialVotos.push(nvoto);
        votante.marcarComoVotado();
        System.out.println("Voto exitosamente registrado");
    }
    public void reportarVoto(Candidato candidato, int idVoto){
        for (Voto v : votosReportados) {
            if (v.getId() == idVoto) {
                System.out.println("El voto ya existe,posible fraude");
                return;
            }
        }
        Queue<Voto> votosTemp = new LinkedList<>();
        boolean encontrado = false;
        while (candidato.getVotosRecibidos().size() > 0){
            Voto v = candidato.getVotosRecibidos().poll();

            if (v.getId() == idVoto) {
                votosReportados.add(v);
                System.out.println("Voto con el ID " + idVoto + " fue reportado correctamente.");
                encontrado = true;
            } else {
                votosTemp.add(v);
            }
        }
        candidato.setVotosRecibidos(votosTemp);
        if (encontrado== false) {
            System.out.println("no se pudo encontrar el voto con la id" + idVoto+" en los votos del candidato");
        }
    }
    public Map<Integer, Integer> obtenerResultados() {
        Map<Integer, Integer> resultados = new HashMap<>();

        for (Candidato c : listaCandidato) {
            System.out.println(c.getNombre() + ", Votos: " + c.getVotosRecibidos().size());
            resultados.put(c.getId(), c.getVotosRecibidos().size());
        }
        return resultados;

    }

}

public class Main{
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        UrnaElectoral u = new UrnaElectoral();

        u.getListaCandidato().add(new Candidato(1, "Leandro Cares", "Partido A"));
        u.getListaCandidato().add(new Candidato(2, "Matias Camus", "Partido B"));
        u.getListaCandidato().add(new Candidato(3, "Benjamin Teillier", "Partido C"));

        Votante votante1 = new Votante(1, "Diego");
        Votante votante2 = new Votante(2, "Antonella");
        Votante votante3 = new Votante(3, "Esteban");

        while(true){
            System.out.println("   MENÚ   ");
            System.out.println("1. Votar");
            System.out.println("2. Ver Resultados");
            System.out.println("3. Reportar Voto");
            System.out.println("4. Salir");
            System.out.println("Ingresar opción: ");
            int opcion = sc.nextInt();

            switch(opcion){
                case 1:
                    System.out.println("Opcion 1 ");
                    System.out.println("Lista de votantes: ");
                    System.out.println("1. " + votante1.getNombre() );
                    System.out.println("2. " + votante2.getNombre() );
                    System.out.println("3. " + votante3.getNombre() );
                    System.out.println("Ingrese su ID para votar: ");
                    int seleccion = sc.nextInt();

                    Votante votante = null;

                    if(seleccion == 1){
                        votante = votante1;
                    }
                    else if(seleccion == 2){
                        votante = votante2;
                    }
                    else if(seleccion == 3){
                        votante = votante3;
                    }
                    else{
                        System.out.println("Opcion invalida");
                    }

                    if(votante.getYaVoto() == true){
                        System.out.println("El votante con el ID " + votante.getId() + " ya ha votado");
                        break;
                    }

                    System.out.println("Lista de candidatos: ");
                    System.out.println("1. " + u.getListaCandidato().get(0).getNombre() );
                    System.out.println("2. " + u.getListaCandidato().get(1).getNombre() );
                    System.out.println("3. " + u.getListaCandidato().get(2).getNombre() );

                    System.out.print("Ingrese el ID del candidato para votar: ");
                    int idCandidato = sc.nextInt();
                    u.registrarVoto(votante, idCandidato);
                    break;

                case 2:
                    System.out.println("Opcion 2 ");
                    u.obtenerResultados();
                    break;

                case 3:


                    System.out.println("Opción 3 ");
                    System.out.println("Seleccione al candidato");
                    for(int i = 0; i < u.getListaCandidato().size(); i++){
                        System.out.println( i+1 + ". " + u.getListaCandidato().get(i).getNombre() );
                    }
                    int seleccion2 = sc.nextInt() - 1;

                    if(seleccion2 < 0 || seleccion2 > u.getListaCandidato().size()){
                        System.out.println("Opcion invalida");
                        break;
                    }

                    Candidato candidato = u.getListaCandidato().get(seleccion2);

                    System.out.println("Lista de votos del candidato " + candidato.getNombre() + ": ");

                    for(Voto v : candidato.getVotosRecibidos()){
                        System.out.println("ID del voto: " + v.getId() + " con fecha: " + v.getTimestamp());
                    }
                    System.out.println("Ingrese el ID del voto para reportar: ");
                    int vReport = sc.nextInt();

                    u.reportarVoto(candidato, vReport);
                    break;

                case 4:
                    return;
            }
        }
    }
}