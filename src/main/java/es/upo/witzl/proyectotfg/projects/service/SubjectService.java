package es.upo.witzl.proyectotfg.projects.service;

import es.upo.witzl.proyectotfg.projects.dto.SubjectDto;
import es.upo.witzl.proyectotfg.projects.error.SubjectAlreadyExistsException;
import es.upo.witzl.proyectotfg.projects.model.Project;
import es.upo.witzl.proyectotfg.projects.model.Subject;
import es.upo.witzl.proyectotfg.projects.repository.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubjectService implements ISubjectService {

    @Autowired
    SubjectRepository subjectRepository;

    @Override
    public Subject registerSubjectToProject(SubjectDto subjectDto, Project project) {
        if(project.getSubject() != null) {
            throw new SubjectAlreadyExistsException();
        }
        Subject subject = new Subject();
        subject.setBirthDate(subjectDto.getBirthDate());
        subject.setGender(subjectDto.getGender());
        subject.setWeight(subjectDto.getWeight());
        subject.setHeight(subjectDto.getHeight());
        subject.setSmoker(subjectDto.isSmoker());
        subject.setProject(project);

        return subjectRepository.save(subject);
    }
}
