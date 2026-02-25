package com.example.userlogin;

import com.example.userlogin.service.UserService;
import com.example.userlogin.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * 分页查询功能测试
 * 
 * 演示如何使用分页 API：
 * 1. 分页获取所有用户
 * 2. 按用户名搜索用户
 * 3. 按邮箱搜索用户
 * 4. 支持排序和自定义分页大小
 */
@SpringBootTest
public class PaginationTest {

    @Autowired
    private UserService userService;

    /**
     * 测试基本分页功能
     */
    @Test
    public void testBasicPagination() {
        // 获取第一页，每页10条记录
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<UserVO> page = userService.getAllUsers(pageRequest);
        
        // 验证分页结果
        assertNotNull(page);
        assertFalse(page.isEmpty() || page.getContent().size() > 0 || page.getContent().size() == 0);
        System.out.println("第一页用户数: " + page.getNumberOfElements());
        System.out.println("总页数: " + page.getTotalPages());
        System.out.println("总记录数: " + page.getTotalElements());
    }

    /**
     * 测试带排序的分页
     */
    @Test
    public void testPaginationWithSort() {
        // 按用户名升序排列，每页20条
        PageRequest pageRequest = PageRequest.of(
            0, 
            20, 
            Sort.by(Sort.Direction.ASC, "username")
        );
        Page<UserVO> page = userService.getAllUsers(pageRequest);
        
        assertNotNull(page);
        System.out.println("按用户名升序的第一页: " + page.getContent().size() + " 条记录");
    }

    /**
     * 测试按创建时间降序排列
     */
    @Test
    public void testPaginationSortByCreatedAt() {
        // 按创建时间降序（最新的用户在前）
        PageRequest pageRequest = PageRequest.of(
            0, 
            10, 
            Sort.by(Sort.Direction.DESC, "createdAt")
        );
        Page<UserVO> page = userService.getAllUsers(pageRequest);
        
        assertNotNull(page);
        System.out.println("按创建时间降序的用户:");
        page.getContent().forEach(user -> 
            System.out.println("  - " + user.getUsername() + " (" + user.getCreatedAt() + ")")
        );
    }

    /**
     * 测试用户名搜索（模糊查询）
     */
    @Test
    public void testSearchByUsername() {
        // 搜索用户名包含"admin"的用户
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<UserVO> searchResult = userService.searchUsersByUsername("admin", pageRequest);
        
        assertNotNull(searchResult);
        System.out.println("搜索'admin'的结果数: " + searchResult.getTotalElements());
        searchResult.getContent().forEach(user ->
            System.out.println("  - " + user.getUsername() + " (" + user.getEmail() + ")")
        );
    }

    /**
     * 测试邮箱搜索（模糊查询）
     */
    @Test
    public void testSearchByEmail() {
        // 搜索邮箱包含"example"的用户
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<UserVO> searchResult = userService.searchUsersByEmail("example", pageRequest);
        
        assertNotNull(searchResult);
        System.out.println("搜索'example'邮箱的结果数: " + searchResult.getTotalElements());
        searchResult.getContent().forEach(user ->
            System.out.println("  - " + user.getUsername() + " <" + user.getEmail() + ">")
        );
    }

    /**
     * 测试分页导航
     */
    @Test
    public void testPaginationNavigation() {
        int pageSize = 5;
        PageRequest firstPage = PageRequest.of(0, pageSize);
        Page<UserVO> page = userService.getAllUsers(firstPage);
        
        System.out.println("总页数: " + page.getTotalPages());
        System.out.println("当前页: " + (page.getNumber() + 1));
        System.out.println("是否有下一页: " + page.hasNext());
        System.out.println("是否有上一页: " + page.hasPrevious());
        System.out.println("是否是第一页: " + page.isFirst());
        System.out.println("是否是最后一页: " + page.isLast());
        
        // 获取下一页
        if (page.hasNext()) {
            PageRequest nextPage = PageRequest.of(1, pageSize);
            Page<UserVO> nextPageData = userService.getAllUsers(nextPage);
            System.out.println("下一页的用户数: " + nextPageData.getNumberOfElements());
        }
    }

    /**
     * 测试多字段排序
     */
    @Test
    public void testMultiFieldSort() {
        // 先按创建时间降序，再按用户名升序
        PageRequest pageRequest = PageRequest.of(
            0,
            10,
            Sort.by(
                Sort.Order.desc("createdAt"),
                Sort.Order.asc("username")
            )
        );
        Page<UserVO> page = userService.getAllUsers(pageRequest);
        
        assertNotNull(page);
        System.out.println("多字段排序结果:");
        page.getContent().forEach(user ->
            System.out.println("  - " + user.getUsername() + " (创建于: " + user.getCreatedAt() + ")")
        );
    }

    /**
     * 测试深分页
     */
    @Test
    public void testDeepPagination() {
        // 模拟获取第10页的数据
        PageRequest pageRequest = PageRequest.of(9, 10);  // 第10页，每页10条
        Page<UserVO> page = userService.getAllUsers(pageRequest);
        
        System.out.println("第10页的数据:");
        System.out.println("  当前页: " + (page.getNumber() + 1));
        System.out.println("  本页记录数: " + page.getNumberOfElements());
        System.out.println("  总记录数: " + page.getTotalElements());
    }

    /**
     * 整合测试：完整的分页查询流程
     */
    @Test
    public void testCompleteWorkflow() {
        System.out.println("========== 完整分页查询工作流 ==========");
        
        // 1. 获取第一页数据
        System.out.println("\n1. 获取第一页用户列表:");
        PageRequest firstPageRequest = PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<UserVO> firstPage = userService.getAllUsers(firstPageRequest);
        
        System.out.println("   - 总记录数: " + firstPage.getTotalElements());
        System.out.println("   - 总页数: " + firstPage.getTotalPages());
        System.out.println("   - 本页记录: " + firstPage.getNumberOfElements());
        firstPage.getContent().forEach(user ->
            System.out.println("     * " + user.getUsername() + " - " + user.getEmail())
        );
        
        // 2. 搜索特定用户
        System.out.println("\n2. 搜索用户名为'user'的用户:");
        Page<UserVO> searchResult = userService.searchUsersByUsername("user", PageRequest.of(0, 10));
        System.out.println("   - 搜索结果数: " + searchResult.getTotalElements());
        searchResult.getContent().forEach(user ->
            System.out.println("     * " + user.getUsername())
        );
        
        // 3. 按邮箱搜索
        System.out.println("\n3. 搜索@example.com的邮箱:");
        Page<UserVO> emailSearchResult = userService.searchUsersByEmail("example", PageRequest.of(0, 10));
        System.out.println("   - 搜索结果数: " + emailSearchResult.getTotalElements());
        emailSearchResult.getContent().forEach(user ->
            System.out.println("     * " + user.getEmail())
        );
        
        // 4. 获取下一页
        if (firstPage.hasNext()) {
            System.out.println("\n4. 获取下一页:");
            PageRequest nextPageRequest = PageRequest.of(1, 5, Sort.by(Sort.Direction.DESC, "createdAt"));
            Page<UserVO> nextPage = userService.getAllUsers(nextPageRequest);
            System.out.println("   - 下一页记录数: " + nextPage.getNumberOfElements());
            nextPage.getContent().forEach(user ->
                System.out.println("     * " + user.getUsername() + " - " + user.getEmail())
            );
        }
        
        System.out.println("\n========== 工作流完成 ==========");
    }
}
