import java.util.List;
import org.springframework.web.context.request.RequestContextHolder

public class DomainService
{
    def messageSource
    
    //--------------------------------------------------------------------------
    Map GetRouteMap(Map params)
    {
        Route route_instance = Route.get(params.id)

        if(!route_instance) {
            return ['message':getMsg('fc.notfound',[getMsg('fc.route'),params.id])]
        }
        
        return ['instance':route_instance]
    }
    
    //--------------------------------------------------------------------------
    Map GetTaskMap(Map params)
    {
        Task task_instance = Task.get(params.id)

        if (!task_instance) {
            return ['message':getMsg('fc.notfound',[getMsg('fc.task'),params.id])]
        }
        
        return ['instance':task_instance]
    }

    //--------------------------------------------------------------------------
    Map GetTestMap(Map params)
    {
        Test test_instance = Test.get(params.id)

        if (!test_instance) {
            return ['message':getMsg('fc.notfound',[getMsg('fc.test'),params.id])]
        }
        
        return ['instance':test_instance]
    }

    //--------------------------------------------------------------------------
    private String getMsg(String code, List args)
    {
        def session_obj = RequestContextHolder.currentRequestAttributes().getSession()
        if (args) {
            return messageSource.getMessage(code, args.toArray(), new Locale(session_obj.showLanguage))
        } else {
            return messageSource.getMessage(code, null, new Locale(session_obj.showLanguage))
        }
    }

    //--------------------------------------------------------------------------
    private String getMsg(String code)
    {
        def session_obj = RequestContextHolder.currentRequestAttributes().getSession()
        return messageSource.getMessage(code, null, new Locale(session_obj.showLanguage))
    }
    
    //--------------------------------------------------------------------------
    private String getPrintMsg(String code)
    {
        def session_obj = RequestContextHolder.currentRequestAttributes().getSession()
        return messageSource.getMessage(code, null, new Locale(session_obj.printLanguage))
    }
    
}
