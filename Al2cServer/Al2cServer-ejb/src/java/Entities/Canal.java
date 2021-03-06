package Entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Entité des canaux
 * 
 * @author fez
 * @author Alexandre Bertrand
 */
@Entity
@Table(name = "canal")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Canal.findAll", query = "SELECT c FROM Canal c")
    , @NamedQuery(name = "Canal.findById", query = "SELECT c FROM Canal c WHERE c.id = :id")
    , @NamedQuery(name = "Canal.findByValeur", query = "SELECT c FROM Canal c WHERE c.valeur = :valeur")
    , @NamedQuery(name = "Canal.findByTypeCanal", query = "SELECT c FROM Canal c WHERE c.typeCanal = :typeCanal")
    , @NamedQuery(name = "Canal.findByConversationId", query = "SELECT c FROM Canal c WHERE c.conversationId = :conversationId")})
public class Canal implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    
    @Size(max = 80)
    @Column(name = "conversation_id")
    private String conversationId;
    
    @Size(max = 80)
    @Column(name = "valeur")
    private String valeur;
    
    @Column(name = "type_canal")
    private String typeCanal;
    
    @Column(name = "reponse")
    private Boolean reponse;
    
    @JoinColumn(name = "contact_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Contact contactId;

    public Canal() {
    }

    public Canal(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getValeur() {
        return valeur;
    }

    public void setValeur(String valeur) {
        this.valeur = valeur;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getTypeCanal() {
        return typeCanal;
    }

    public void setTypeCanal(String typeCanal) {
        this.typeCanal = typeCanal;
    }
    
    public Boolean getReponse() {
        return reponse;
    }
    
    public void setReponse(Boolean reponse) {
        this.reponse = reponse;
    }
    
    public Contact getContactId() {
        return contactId;
    }

    public void setContactId(Contact contactId) {
        this.contactId = contactId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Canal)) {
            return false;
        }
        Canal other = (Canal) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entities.Canal[ id=" + id + " ]";
    }
    
}
