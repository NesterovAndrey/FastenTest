package core.data;

public class CadastralNumber implements Comparable<CadastralNumber>{
    private final String ca;
    private final String cd;
    private final String cb;
    private final String cp;

    public CadastralNumber(String cd,String ca, String cb, String cp) {
        this.cd = cd;
        this.ca = ca;
        this.cb = cb;
        this.cp = cp;
    }

    public String getCa() {
        return ca;
    }

    public String getCd() {
        return cd;
    }

    public String getCb() {
        return cb;
    }

    public String getCp() {
        return cp;
    }

    @Override
    public String toString() {
        return String.format("%s:%s:%s:%s",getCd(),getCa(),getCb(),getCp());
    }

    @Override
    public int compareTo(CadastralNumber o) {
       if(this==o) return 0;
       if(o==null) return 1;
        int result;
       result=Integer.valueOf(o.getCd()).compareTo(Integer.valueOf(this.getCd()));
       if(result!=0)
           return result;
       result=Integer.valueOf(o.getCa()).compareTo(Integer.valueOf(this.getCa()));
        if(result!=0)
            return result;
        result=Integer.valueOf(o.getCb()).compareTo(Integer.valueOf(this.getCb()));
        if(result!=0)
            return result;
        return Integer.valueOf(o.getCp()).compareTo(Integer.valueOf(this.getCb()));
    }
}
