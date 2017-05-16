package Metier;

import Controllers.ContactFacade;
import Controllers.UtilisateurFacade;
import Entities.Contact;
import Entities.Utilisateur;
import Exception.noContactExistsException;
import Exception.notFoundUtilisateurException;
import Exception.unknowUserIdException;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 * Implémentation de la gestion des contacts
 *
 * @author Alexandre Bertrand
 */
@Stateless
public class gestionContact implements IgestionContact {

    @EJB
    private ContactFacade contactFacade;
    
    @EJB
    private UtilisateurFacade utilisateurFacade;

    @Override
    public boolean hasContact(int idUtilisateur) {
        Utilisateur u = utilisateurFacade.find(idUtilisateur);
        if (u.getContactCollection().isEmpty())
            return false;
        return true;
    }
    
    @Override
    public List<Contact> getListeContacts(int idUtilisateur)
            throws noContactExistsException, unknowUserIdException {
        try {
            Utilisateur u = utilisateurFacade.find(idUtilisateur);
            if (u.getContactCollection().isEmpty())
                throw new noContactExistsException();

            return (List<Contact>) u.getContactCollection();
        } catch (Exception e) {
            throw new unknowUserIdException();
        }
    }

    @Override
    public int ajouterContact(int idUtilisateur, String nom, String prenom)
            throws notFoundUtilisateurException {
        // vérifier qu'aucun des champs n'est vide
        try {
            Utilisateur u = utilisateurFacade.find(idUtilisateur);

            Contact contact = new Contact();
            contact.setNom(nom);
            contact.setPrenom(prenom);
            contact.setUtilisateurId(u);
            
            contactFacade.create(contact);
            return contact.getId();
        } catch (Exception e) {
            System.out.println("test " + e.getMessage());
            throw new notFoundUtilisateurException();
        }
    }

    @Override
    public void modifierContact(int idContact, int idUtilisateur, String nom,
            String prenom) throws noContactExistsException {
        try {
            if (!isContactExistsInUtilisateurContacts(idContact, idUtilisateur))
                throw new noContactExistsException();
            Contact contact = contactFacade.find(idContact);
            contact.setNom(nom);
            contact.setPrenom(prenom);

            contactFacade.edit(contact);
        } catch (Exception e) {
            throw new noContactExistsException();
        }
    }

    @Override
    public Contact afficherContact(int idContact, int idUtilisateur)
            throws noContactExistsException {
        try {
            if (!isContactExistsInUtilisateurContacts(idContact, idUtilisateur))
                throw new noContactExistsException();
            System.out.println("Metier.gestionContact.afficherContact()");
            return contactFacade.find(idContact);
        } catch (Exception e) {
            System.out.println("echec");
            throw new noContactExistsException();
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
            List<Contact> contacts = (List<Contact>) utilisateurFacade
                .find(idUtilisateur).getContactCollection();
           
            for(Contact c: contacts){
                if(c.getId().equals(idContact))
                    return true;
            }
          return false;
            
        } catch (Exception e) {
            return false;
        }
    }
    
}
