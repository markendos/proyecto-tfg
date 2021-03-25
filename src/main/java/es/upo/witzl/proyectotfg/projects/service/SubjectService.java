package es.upo.witzl.proyectotfg.projects.service;

import es.upo.witzl.proyectotfg.projects.dto.SubjectDto;
import es.upo.witzl.proyectotfg.projects.error.SubjectAlreadyExistsException;
import es.upo.witzl.proyectotfg.projects.model.Project;
import es.upo.witzl.proyectotfg.projects.model.Subject;
import es.upo.witzl.proyectotfg.projects.repository.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

@Service
public class SubjectService implements ISubjectService {

    @Autowired
    SubjectRepository subjectRepository;

    @Override
    public Subject saveSubject(SubjectDto subjectDto, Project project) {
        Subject subject;
        if(project.getSubject() != null) {
            subject = project.getSubject();
        } else {
            subject = new Subject();
        }
        subject.setBirthDate(subjectDto.getBirthDate());
        subject.setGender(subjectDto.getGender());
        subject.setWeight(subjectDto.getWeight());
        subject.setHeight(subjectDto.getHeight());
        subject.setSmoker(subjectDto.isSmoker());
        subject.setProject(project);
        if(subjectDto.getHeight() != null && subjectDto.getWeight() != null) {
            subject.setBmi(calculateBmi(subject));
            subject.setBfp(calculateBfp(subject));
        }else {
            subject.setBmi(null);
            subject.setBfp(null);
        }

        return subjectRepository.save(subject);
    }

    @Override
    public void deleteSubject(Subject subject) {
        subjectRepository.delete(subject);
    }

    private Float calculateBmi(Subject subject) {
        float weight = subject.getWeight();
        float height = subject.getHeight()/100;

        return Float.valueOf(weight/((float) Math.pow(height, 2)));
    }

    private Float calculateBfp(Subject subject) {
        float bmi = subject.getBmi();
        float bmi2 = bmi * bmi;
        int age = getDiffYears(subject.getBirthDate(), new Date());
        int gender = subject.getGender() == "m" ? 1 : 0;
        double result = (-44.988 + (0.503 * age) + (10.689 * gender) + (3.172 * bmi) - (0.026 * bmi2) +
                (0.181 * bmi * gender) - (0.02 * bmi * age) - (0.005 * bmi2 * gender) + (0.00021 * bmi2 * age));
        return Float.valueOf((float) result);

    }

    private int getDiffYears(Date first, Date last) {
        Calendar a = getCalendar(first);
        Calendar b = getCalendar(last);
        int diff = b.get(Calendar.YEAR) - a.get(Calendar.YEAR);
        if (a.get(Calendar.DAY_OF_YEAR) > b.get(Calendar.DAY_OF_YEAR)) {
            diff--;
        }
        return diff;
    }

    public static Calendar getCalendar(Date date) {
        Calendar cal = Calendar.getInstance(Locale.getDefault());
        cal.setTime(date);
        return cal;
    }
}
