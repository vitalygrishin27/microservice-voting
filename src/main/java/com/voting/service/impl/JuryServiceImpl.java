package com.voting.service.impl;

import com.voting.bom.Jury;
import com.voting.exception.CategoryException;
import com.voting.exception.JuryException;

import com.voting.repository.JuryRepo;

import com.voting.service.JuryService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;


@Service
public class JuryServiceImpl implements JuryService {
    @Autowired
    JuryRepo repo;

    @Value("${maxUploadFileSizePlayerPhoto}")
    private Long maxUploadFileSizePlayerPhoto;

    @Override
    public Long create(Jury jury) {
        try {
            checkFieldsAndSetFile(jury);
            repo.saveAndFlush(jury);
        } catch (Exception e) {
            throw new CategoryException(e.getMessage());
        }
        return jury.getId();
    }

    @Override
    public void delete(Long juryId) {
        repo.delete(getJuryIfExists(juryId));
    }

    @Override
    public Long update(Jury jury) {
        try {
            checkFieldsAndSetFile(jury);
            repo.saveAndFlush(jury);
        } catch (Exception e) {
            throw new CategoryException(e.getMessage());
        }
        return jury.getId();
    }

    @Override
    public List<Jury> getAll() {
        return repo.findAll();
    }

    private Jury getJuryIfExists(Long juryId) {
        Optional<Jury> optionalJury = repo.findById(juryId);
        if (optionalJury.isEmpty()) {
            throw new JuryException(String.format("Jury with id=%s not found", juryId));
        }
        return optionalJury.get();
    }

    private void checkFieldsAndSetFile(Jury jury) throws IOException {
        if (jury.getFirstName().equals("") ||
                jury.getLastName().equals("") ||
                jury.getLogin().equals("")) throw new JuryException("Mandatory fields are required");
        if (jury.getPassword() != null) jury.setEncryptedPassword(encryptPassword(jury.getPassword()));
        if (jury.getFile() != null && jury.getFile().getSize() > maxUploadFileSizePlayerPhoto)
            throw new JuryException("File size is too large. Allowed to " + maxUploadFileSizePlayerPhoto);
        if (jury.getFile() != null)
            jury.setPhoto("data:image/jpeg;base64, " + Base64Utils.encodeToString(jury.getFile().getBytes()));
    }

    public static String encryptPassword(String password) {
        return Base64.getEncoder().encodeToString(password.getBytes());
    }

    public static Boolean isPasswordMatch(String password, String encryptedPassword) {
        return new String(Base64.getDecoder().decode(encryptedPassword)).equals(password);
    }
}
