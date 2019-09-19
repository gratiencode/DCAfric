/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stateless;

import javax.ejb.ApplicationException;

/**
 *
 * @author PC
 */
@ApplicationException(rollback = true)
public class ValidationException extends RuntimeException{

    public ValidationException(String message) {
        super(message);
    }
    
}
