import java.io.*; // for BufferedReader
import java.util.*; // for StringTokenizer

    class Edge_Node {
        private Vertex_Node target;
        private Edge_Node next;
        public Edge_Node(Vertex_Node t, Edge_Node e) {
            target = t;
            next = e;
        }

        public Vertex_Node getTarget() {
            return target;
        }
        public Edge_Node getNext() {
            return next;
        }
    }

 class Vertex_Node {
    private String name;
    private Edge_Node edge_head;
    private int distance;
    private Vertex_Node parent;
    private Vertex_Node next;
    private boolean marked; //automatically false on creation

    //constructor
    public Vertex_Node(String s, Vertex_Node v) {
        name = s;
        next = v;
        distance = -1;
        boolean marked = false;
    }
    public String getName() {
        return name;
    }
    public int getDistance() {
        return distance;
    }

    public void setDistance(int d) {
        this.distance=d;
    }

    //neighbor list
    public Edge_Node getNbrList() {
        return this.edge_head;
    }

    // neighbor list
    public void setNbrList(Edge_Node e) {
        this.edge_head = e;
    }

    public Vertex_Node getNext() {
        return next;
    }

    public Vertex_Node getParent() {
        return parent;
    }

    public void setParent (Vertex_Node n){
        this.parent=n;
    }

    public boolean getVisited (){
        return this.marked;
    }

    public void setVisited (boolean visit){
        marked = visit;
    }

    // reset all distance values to -1
    public void clearDist() {
        distance = -1;
    }

}


class Graph {
    private Vertex_Node head;
    private int size;
    public Graph() {
        head = null;
        size = 0;
    }

    public void clearAll(){
        Vertex_Node current = this.head;
        while(current != null){
            current.setDistance(-1);
            current.setVisited(false);
            current = current.getNext();
        }
    }

    public Vertex_Node findVertex(String s) {
        Vertex_Node pt = head;
        while (pt != null && s.compareTo(pt.getName()) != 0) {
            pt = pt.getNext();
        }
        return pt;
    }

    public Vertex_Node input(String fileName) throws IOException {
        String inputLine, sourceName, targetName;
        Vertex_Node source = null, target;
        Edge_Node e;
        StringTokenizer input;
        BufferedReader inFile = new BufferedReader(new FileReader(fileName));
        inputLine = inFile.readLine();
        while (inputLine != null) {
            input = new StringTokenizer(inputLine);
            sourceName = input.nextToken();
            source = findVertex(sourceName);
            if (source == null) {
                head = new Vertex_Node(sourceName, head);
                source = head;
                size++;
            }
            if (
                    input.hasMoreTokens()) {
                targetName = input.nextToken();
                target = findVertex(targetName);
                if (target == null) {
                    head = new Vertex_Node(targetName, head);
                    target = head;
                    size++;
                }
// put edge in one direction -- while checking for repeat
                e = source.getNbrList();
                while (e != null) {
                    if (e.getTarget() == target) {
                        System.out.print("Multiple edges from " + source.getName() + " to ");
                        System.out.println(target.getName() + ".");
                        break;
                    }
                    e = e.getNext();
                }
                source.setNbrList(new Edge_Node(target, source.getNbrList()));
// put edge in the other direction
                e = target.getNbrList();
                while (e != null) {
                    if (e.getTarget() == source) {
                        System.out.print("Multiple edges from "
                                + target.getName() + " to ");
                        System.out.println(source.getName() + ".");
                        break;
                    }
                    e = e.getNext();
                }
                target.setNbrList(new Edge_Node(source, target.getNbrList()));
            }
            inputLine = inFile.readLine();
        }
        inFile.close();
        return source;
    }

    public void output() {
        Vertex_Node v = head;
        Edge_Node e;
        while (v != null) {
            System.out.print(v.getName() + ": ");
            e = v.getNbrList();
            while (e != null) {
                System.out.print(e.getTarget().getName() + " ");
                e = e.getNext();
            }
            System.out.println();
            v = v.getNext();
        }
    }

    public Vertex_Node vertex_check(){
      Vertex_Node current = head;
      while (current != null ){
          if (current.getVisited() == true){
             current = current.getNext();
          } else {return current;}
      }
    return current;
    }

    public void output_bfs(Vertex_Node s) {

        Queue<Vertex_Node> queue = new LinkedList<>();
        Vertex_Node qTop;
        Vertex_Node current = s;
        current.setDistance(0);
        current.setVisited(true);
        queue.add(current);

        // while loop to check every node has been marked
        while (!queue.isEmpty()) {

            qTop = queue.remove(); // First element is now out of queue
            Edge_Node edgeHold = qTop.getNbrList(); // gets the neighbor of start vertex

            while (edgeHold != null) {
                current = edgeHold.getTarget();
                if (current.getVisited() == false) {
                    current.setVisited(true);
                    current.setParent(qTop);
                    current.setDistance(qTop.getDistance()+1); // set distance from parent
                    queue.add(current);
                }
                edgeHold = edgeHold.getNext();
            }
            if (qTop.getParent() == null) {
                System.out.println( qTop.getName() + ", " + qTop.getDistance() + ", " + " Null");
            } else {
                System.out.println( qTop.getName() + ", " + qTop.getDistance() + ", " + qTop.getParent().getName());
            }
        }
        if (vertex_check() != null ){
            output_bfs(vertex_check());
        }
        clearAll();
    }

    public void output_dfs(Vertex_Node s) {
        recursive_dfs(s,0);
        //check to make sure this
        if (vertex_check() != null ) {
            output_dfs(vertex_check());
        }
        clearAll();
        //clear all just in case
    }

    public void recursive_dfs(Vertex_Node s, int distance){
       Vertex_Node current = s;
       current.setVisited(true); // set distance in start
       current.setDistance(distance);// passed in distance

        if (current.getParent() == null) {
            System.out.println( current.getName() + ", " + current.getDistance() + ", " + " Null");
        } else {
            System.out.println( current.getName() + ", " + current.getDistance() + ", " + current.getParent().getName());
        } //reuse the print from bfs

        Edge_Node edgeHold = current.getNbrList(); // gets the neighbor of start vertex

        while (edgeHold != null){
            if (edgeHold.getTarget().getVisited() == false) {
                edgeHold.getTarget().setParent(s);
                recursive_dfs(edgeHold.getTarget(), distance + 1);
            } // set parent within the while loop and recursively go down the line
            edgeHold = edgeHold.getNext();
        }
    }

    public static boolean implementedDFS() {
        return true;
    }

    public static String myName() {
        return "Santa Claus";
    }
}
