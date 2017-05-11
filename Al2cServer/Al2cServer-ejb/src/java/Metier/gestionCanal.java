package Metier;

import Controllers.CanalFacade;
import Controllers.ContactFacade;
import Controllers.UtilisateurFacade;
import Entities.Canal;
import Entities.Contact;
import Entities.util.TypeCanal;
import Exception.noCanalFoundException;
import Exception.noContactExistsException;
import Exception.notFoundUtilisateurException;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 * Implémentation de la gestion des canaux
 *
 * @author Alexandre Bertrand
 */
@Stateless
public class gestionCanal implements IgestionCanal {

    @EJB
    private CanalFacade canalFacade;
    
    @EJB
    private ContactFacade contactFacade;
    
    @EJB
    private UtilisateurFacade utilisateurFacade;
    
    @Override
    public void ajouterCanal(int idContact, int idUtilisateur, String valeur,
            TypeCanal typeCanal) throws notFoundUtilisateurException {
        try {
            if (!isContactExistsInUtilisateurContacts(idContact, idUtilisateur))
                throw new noContactExistsException();
            Contact c = contactFacade.find(idContact);
            
            Canal canal = new Canal();
            canal.setValeur(valeur);
            canal.setTypeCanal(typeCanal);
            canal.setContactId(c);

            canalFacade.create(canal);
        } catch (Exception e) {
            throw new notFoundUtilisateurException();
        }
    }
    
    @Override
    public void modifierCanal(int idCanal, int idUtilisateur, String valeur,
            TypeCanal typeCanal) throws noCanalFoundException {
        try {
            Canal canal = canalFacade.find(idCanal);
            if (!isCanalExistsInUtilisateurCanaux(idCanal,
                    canal.getContactId().getId(), idUtilisateur))
                throw new noCanalFoundException();

            canal.setValeur(valeur);
            canal.setTypeCanal(typeCanal);

            canalFacade.edit(canal);
        } catch (Exception e) {
            throw new noCanalFoundException();
        }
    }

    @Override
    public void supprimerCanal(int idCanal, int idUtilisateur)
            throws noCanalFoundException {
        try {
            Canal canal = canalFacade.find(idCanal);
            if (!isCanalExistsInUtilisateurCanaux(idCanal,
                    canal.getContactId().getId(), idUtilisateur))
                throw new noCanalFoundException();
            
            canalFacade.remove(canal);
        } catch (Exception e) {
            throw new noCanalFoundException();
        }
    }
    
    /**
     * Vérification de l'appartenance d'un canal à un utilisateur
     * @param idCanal identifiant du contact
     * @param idUtilisateur identifiant de l'utilisateur
     * @return true si le contact existe et appartien à l'utilisateur
     *         sinon retourn false
     */
    private boolean isCanalExistsInUtilisateurCanaux(int idCanal, int idContact,
            int idUtilisateur) {
        try {
            List<Contact> contact = (List<Contact>) utilisateurFacade
                .find(idUtilisateur).getContactCollection();
            if (contact.stream().anyMatch((e) -> (e.getId().equals(idContact)))) {
                Contact c = contact.get(idContact);
                List<Canal> canal = (List<Canal>) contactFacade
                    .find(idContact).getCanalCollection();
                return canal.stream().anyMatch((e) -> (e.getId().equals(idCanal)));
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Vérification de l'appartenance d'un contact à un utilisateur
     * @param idContact identifiant du contact
     * @param idUtilisateur identifiant de l'utilisateur
     * @return true si le contact existe et appartien à l'utilisateur
     *         sinon retourn false
     */
    private boolean isContactExistsInUtilisateurContacts(int idContact,
            int idUtilisateur) {
        try {
            List<Contact> contact = (List<Contact>) utilisateurFacade
                .find(idUtilisateur).getContactCollection();
            return contact.stream().anyMatch((e) -> (e.getId().equals(idContact)));
        } catch (Exception e) {
            return false;
        }
    }

}
