package core.data;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CadastralEntry implements Comparable<CadastralEntry> {
    private final String ownerPassport;
    private final String ownerId;
    private final Date registrationDate;
    private final CadastralNumber cadastralNumber;

    public CadastralEntry(String ownerPassport, String ownerId, Date registrationDate, CadastralNumber cadastralNumber) {
        this.ownerPassport = ownerPassport;
        this.ownerId = ownerId;
        this.registrationDate = registrationDate;
        this.cadastralNumber = cadastralNumber;
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
        simpleDateFormat.setLenient(false);
        String str=simpleDateFormat.format(registrationDate);
    }

    public String getOwnerPassport() {
        return ownerPassport;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public CadastralNumber getCadastralNumber() {
        return cadastralNumber;
    }

    @Override
    public int compareTo(CadastralEntry o) {
        if(o==null)
        {
            return 1;
        }
        if(o==this) return 0;
        int result=o.getRegistrationDate().compareTo(this.getRegistrationDate());
        if(result==0)
        {
            result=o.getCadastralNumber().compareTo(this.getCadastralNumber());
        }

        return result;
    }
}
