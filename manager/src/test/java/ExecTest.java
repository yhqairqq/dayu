import com.alibaba.otter.shared.common.utils.cmd.Exec;

/**
 * Created by huaseng on 2019/10/8.
 */
public class ExecTest {

    public static void main(String[] args) throws Exception {
        Exec.Result result = Exec.execute("pwd");
        System.out.println(result);
    }
}
